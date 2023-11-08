package com.capstone.backend.service.impl;

import com.capstone.backend.entity.*;
import com.capstone.backend.entity.type.*;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.comment.CommentDetailDTOResponse;
import com.capstone.backend.model.dto.materials.DataMaterialsDTOResponse;
import com.capstone.backend.model.dto.materials.MaterialsFilterDTORequest;
import com.capstone.backend.model.dto.resource.*;
import com.capstone.backend.model.dto.tag.TagSuggestDTORequest;
import com.capstone.backend.model.dto.tag.TagSuggestDTOResponse;
import com.capstone.backend.model.dto.userresource.UserResourceRequest;
import com.capstone.backend.model.mapper.*;
import com.capstone.backend.repository.*;
import com.capstone.backend.repository.criteria.MaterialsCriteria;
import com.capstone.backend.repository.criteria.ResourceCriteria;
import com.capstone.backend.service.*;
import com.capstone.backend.utils.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.capstone.backend.utils.Constants.CREATOR_RESOURCE_PERMISSION;
import static com.capstone.backend.utils.Constants.CREATOR_RESOURCE_PERMISSION_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResourceServiceImpl implements ResourceService {
    ResourceRepository resourceRepository;
    UserResourceRepository userResourceRepository;
    CommentService commentService;
    FileService fileService;
    TagRepository tagRepository;
    LessonRepository lessonRepository;
    SubjectRepository subjectRepository;
    UserHelper userHelper;
    ResourceTagRepository resourceTagRepository;
    UserResourcePermissionRepository userResourcePermissionRepository;
    ResourceCriteria resourceCriteria;
    MessageException messageException;
    MaterialsCriteria materialsCriteria;
    UserRepository userRepository;
    UserRoleRepository userRoleRepository;
    CheckPermissionResource checkPermissionResource;
    UserResourceService userResourceService;
    ResourceTagService resourceTagService;

    private void saveToResourceTag(ResourceDTORequest request, Resource resource) {
        if (request.getTagList() != null) {
            Set<Long> tagIds = DataHelper.parseStringToLongSet(request.getTagList());
            List<ResourceTag> resourceTagList = tagIds.stream()
                    .map(tagId -> {
                        Tag tagObject = tagRepository.findById(tagId)
                                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_TAG_NOT_FOUND));
                        return ResourceTag.builder()
                                .tag(tagObject)
                                .resource(resource)
                                .detailId(resource.getId())
                                .tableType(TableType.resource_tbl)
                                .active(true)
                                .createdAt(LocalDateTime.now())
                                .build();
                    })
                    .collect(Collectors.toList());
            resource.setResourceTagList(resourceTagList);
        }
    }

    // =================================================================================================================

    @Override
    public List<ResourceDTOResponse> uploadResource(ResourceDTORequest request, MultipartFile[] files) {
        List<FileDTOResponse> fileDTOResponseList = fileService.uploadMultiFile(files, request.getLessonId());
        Subject subject = subjectRepository.findByIdAndActiveTrue(request.getSubjectId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_SUBJECT_NOT_FOUND));

        Lesson lesson = (request.getLessonId() != null)
                ? lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_LESSON_NOT_FOUND))
                : null;

        return fileDTOResponseList.stream()
                .map(fileDTOResponse -> {
                    // Check document or media
                    boolean isMedia = ResourceType.getFeeList().stream()
                            .anyMatch(rt -> fileDTOResponse.getFileExtension().equalsIgnoreCase(rt.toString()));
                    Long pointResource = isMedia ? Constants.POINT_RESOURCE : 0;

                    TabResourceType tabResourceType = TabResourceType.findByTabResourceType(fileDTOResponse.getResourceType());
                    Resource resource = Resource.builder()
                            .name(request.getName())
                            .description(request.getDescription())
                            .resourceType(fileDTOResponse.getResourceType())
                            .createdAt(LocalDateTime.now())
                            .active(true)
                            .approveType(ApproveType.UNACCEPTED)
                            .visualType(VisualType.valueOf(request.getVisualType()))
                            .thumbnailSrc(fileDTOResponse.getThumbnailSrc())
                            .resourceSrc(fileDTOResponse.getResourceSrc())
                            .point(pointResource)
                            .size(fileDTOResponse.getSize())
                            .lesson(lesson)
                            .subject(subject)
                            .author(userHelper.getUserLogin())
                            .tabResourceType(tabResourceType)
                            .build();

                    resource = resourceRepository.save(resource);
                    saveToResourceTag(request, resource);

                    UserResourcePermission permission = UserResourcePermission.builder()
                            .user(userHelper.getUserLogin())
                            .resource(resource)
                            .active(true)
                            .createdAt(LocalDateTime.now())
                            .permission(CREATOR_RESOURCE_PERMISSION)
                            .build();
                    userResourcePermissionRepository.save(permission);

                    return ResourceMapper.toResourceDTOResponse(resource);
                })
                .collect(Collectors.toList());
    }

    // =================================================================================================================

    @Override
    public ResourceDetailDTOResponse getResourceDetailById(Long resourceId) {
        User userLoggedIn = userHelper.getUserLogin();
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_RESOURCE_NOT_FOUND));

        ResourceDTOResponse resourceDTOResponse = ResourceMapper.toResourceDTOResponse(resource);

        boolean isPermission = checkPermissionResource.needCheckPermissionResource(userLoggedIn, resource, PermissionResourceType.V);
        if (!isPermission) {
            throw ApiException.forBiddenException(messageException.MSG_NO_PERMISSION);
        }

        boolean isLike = isUserActionType(userLoggedIn, resourceId, ActionType.LIKE);
        boolean isUnlike = isUserActionType(userLoggedIn, resourceId, ActionType.UNLIKE);
        long numberOfLike = countUserActionType(ActionType.LIKE, resourceId);
        long numberOfUnlike = countUserActionType(ActionType.UNLIKE, resourceId);

        List<CommentDetailDTOResponse> commentDetailDTOResponseList = commentService.getListCommentDetailDTOResponse(resourceId);
        List<String> listTagNames = resourceTagRepository.findAllTagName(resourceId);

        List<ResourceViewDTOResponse> listResourceRelates = getResourceRelates(resource, userLoggedIn, listTagNames);

        User owner = resource.getAuthor();

        return ResourceDetailDTOResponse.builder()
                .isLike(isLike)
                .isUnlike(isUnlike)
                .numberOfLike(numberOfLike)
                .numberOfUnlike(numberOfUnlike)
                .resourceDTOResponse(resourceDTOResponse)
                .commentDetailDTOResponses(commentDetailDTOResponseList)
                .listTagRelate(listTagNames)
                .listResourceRelates(listResourceRelates)
                .isSave(isUserActionType(userLoggedIn, resourceId, ActionType.SAVED))
                .owner(UserMapper.toUserDTOResponse(owner))
                .build();
    }

    private boolean isUserActionType(User user, Long resourceId, ActionType actionType) {
        return user != null && userResourceRepository.findUserResourceHasActionType(user.getId(), resourceId, actionType).isPresent();
    }

    private long countUserActionType(ActionType actionType, Long resourceId) {
        return userResourceRepository.countByActionTypeWithResource(actionType, resourceId);
    }

    private List<ResourceViewDTOResponse> getResourceRelates(Resource resource, User userLoggedIn, List<String> listTagNames) {
        if (ResourceType.getResourceByLesson().contains(resource.getResourceType())) {
            return resourceTagRepository
                    .findAllResourceByLessonIdSameResourceType(resource.getResourceType(), resource.getId(), resource.getLesson().getId())
                    .stream()
                    .map(r -> ResourceMapper.toResourceViewDTOResponse(r, isUserActionType(userLoggedIn, r.getId(), ActionType.SAVED)))
                    .toList();
        } else {
            return resourceTagRepository
                    .findAllResourceByTagNameSameResourceType(resource.getResourceType(), resource.getId(), listTagNames)
                    .stream()
                    .map(r -> ResourceMapper.toResourceViewDTOResponse(r, isUserActionType(userLoggedIn, r.getId(), ActionType.SAVED)))
                    .toList();
        }
    }

    // =================================================================================================================

    private Set<Long> getResourceIdsByCriteria(TableType tableType, Set<ResourceMediaDTOCriteria> criteriaList) {
        Set<Long> tableIds =  criteriaList.stream()
                .filter(criteria -> criteria.getTableType() == tableType)
                .map(ResourceMediaDTOCriteria::getDetailId)
                .collect(Collectors.toSet());

        switch (tableType) {
            case class_tbl -> resourceRepository.findResourceIdClass(tableIds);
            case book_series_tbl -> resourceRepository.findResourceIdBookSeries(tableIds);
            case subject_tbl -> resourceRepository.findResourceIdSubject(tableIds);
            case book_volume_tbl -> resourceRepository.findResourceIdBookVolume(tableIds);
            case chapter_tbl -> resourceRepository.findResourceIdChapter(tableIds);
            case lesson_tbl -> resourceRepository.findResourceIdLesson(tableIds);
        }
        return tableIds;
    }

    @Override
    public PagingResourceDTOResponse searchMediaResource(ResourceMediaDTOFilter resourceDTOFilter) {
        Set<ResourceMediaDTOCriteria> resourceMediaDTOCriteriaList = resourceTagRepository
                .findResourceTagByTagId(resourceDTOFilter.getListTags())
                .stream()
                .map(ResourceMapper::toResourceMediaDTOCriteria)
                .collect(Collectors.toSet());

        // Tạo một Map để lưu trữ các danh sách theo TableType
        Map<TableType, Set<Long>> tableTypeToIdsMap = new HashMap<>();

        // Sử dụng Map để gom nhóm các danh sách dựa trên TableType
        for (TableType type : TableType.values()) {
            Set<Long> ids = getResourceIdsByCriteria(type, resourceMediaDTOCriteriaList);
            if (!ids.isEmpty()) {
                tableTypeToIdsMap.put(type, ids);
            }
        }

        // Tạo một danh sách duy nhất để lưu trữ tất cả các IDs
        Set<Long> listResourceGrouped = new HashSet<>();

        // Thêm tất cả các IDs vào danh sách duy nhất
        for (Set<Long> ids : tableTypeToIdsMap.values()) {
            listResourceGrouped.addAll(ids);
        }

        return resourceCriteria.searchMediaResource(listResourceGrouped, resourceDTOFilter);
    }

    // =================================================================================================================

    @Override
    public DataMaterialsDTOResponse searchMaterials(MaterialsFilterDTORequest request) {
        return materialsCriteria.searchMaterials(request);
    }

    // =================================================================================================================

    @Override
    public org.springframework.core.io.Resource downloadResource(String fileName) {
        Resource document = resourceRepository.findByResourceSrc(fileName)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_RESOURCE_NOT_FOUND));
        User userLoggedIn = userHelper.getUserLogin();

        boolean isPermission = checkPermissionResource
                .needCheckPermissionResource(userLoggedIn, document, PermissionResourceType.D);
        if (!isPermission || userLoggedIn == null)
            throw ApiException.forBiddenException(messageException.MSG_NO_PERMISSION);

        org.springframework.core.io.Resource resource;
        long pointRemain = userLoggedIn.getTotalPoint() - document.getPoint();
        if (pointRemain > 0) {
            resource = fileService.downloadFile(fileName);
            userLoggedIn.setTotalPoint(pointRemain);
            userRepository.save(userLoggedIn);
        } else throw ApiException.forBiddenException(messageException.MSG_FILE_DOWNLOAD_ERROR);

        boolean action = userResourceService.actionResource(UserResourceRequest.builder()
                .actionType(String.valueOf(ActionType.DOWNLOAD))
                .resourceId(document.getId())
                .build());
        if (!action) throw ApiException.badRequestException(messageException.MSG_INTERNAL_SERVER_ERROR);
        return resource;
    }

    // =================================================================================================================

    @Override
    public Boolean shareResource(ResourceSharedDTORequest request) {
        User userLoggedIn = userHelper.getUserLogin();
        Resource resource = resourceRepository.findById(request.getResourceId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_RESOURCE_NOT_FOUND));

        checkPermissionAndOwner(userLoggedIn, resource);

        resource.setVisualType(request.getVisualType());
        resource = resourceRepository.save(resource);

        List<Long> listUserIdShared = userResourcePermissionRepository
                .findByResource(resource.getId(), userLoggedIn.getId()).stream()
                .map(upr -> upr.getUser().getId())
                .toList();

        List<Long> listUserIdShareNew = request.getUserShareIds();

        List<Long> listAdded = listUserIdShareNew.stream().filter(e -> !listUserIdShared.contains(e)).toList();
        List<Long> listDeleted = listUserIdShared.stream().filter(e -> !listUserIdShareNew.contains(e)).toList();

        // Thêm quyền truy cập cho người dùng
        if (!listAdded.isEmpty()) {
            List<UserResourcePermission> userResourcePermissionAdds = addUserPermissions(listAdded, resource);
            userResourcePermissionRepository.saveAll(userResourcePermissionAdds);
        }

        // Xóa quyền truy cập của người dùng
        if (!listDeleted.isEmpty()) {
            deleteUserPermissions(listDeleted, resource);
        }

        return true;
    }

    private void checkPermissionAndOwner(User user, Resource resource) {
        if (user == null || !checkPermissionResource.needCheckPermissionResource(user, resource, PermissionResourceType.C)) {
            throw ApiException.forBiddenException(messageException.MSG_NO_PERMISSION);
        }

        User ownerResource = userResourcePermissionRepository.getUserOwnerResource(resource.getId());
        if (!Objects.equals(user.getId(), ownerResource.getId())) {
            throw ApiException.internalServerException(messageException.MSG_NO_PERMISSION);
        }
    }

    private List<UserResourcePermission> addUserPermissions(List<Long> userIds, Resource resource) {
        return userIds.stream()
                .map(userId -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));
                    return UserResourceMapper.toUserResourcePermission(user, resource);
                })
                .toList();
    }

    private void deleteUserPermissions(List<Long> userIds, Resource resource) {
        userIds.forEach(userId -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));
            userResourcePermissionRepository.findByUserAndResource(user, resource)
                    .ifPresent(userResourcePermissionRepository::delete);
        });
    }

    // =================================================================================================================

    @Override
    public ResourceSharedDTOResponse viewResourceShareById(Long resourceId) {
        var userLoggedIn = userHelper.getUserLogin();
        var ownerResource = userResourcePermissionRepository.getUserOwnerResource(resourceId);
        var resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_RESOURCE_NOT_FOUND));

        //owner of resource can view
        if (!Objects.equals(userLoggedIn.getId(), ownerResource.getId())) {
            throw ApiException.internalServerException(messageException.MSG_NO_PERMISSION);
        }

        //check permission resource
        boolean isPermission = checkPermissionResource
                .needCheckPermissionResource(userLoggedIn, resource, PermissionResourceType.C);
        if (!isPermission)
            throw ApiException.forBiddenException(messageException.MSG_NO_PERMISSION);

        var userResourcePermissions = new ArrayList<>(userResourcePermissionRepository
                .findByResource(resource.getId(), ownerResource.getId()).stream()
                .sorted(Comparator.comparing(up -> up.getUser().getUsername()))
                .toList());

        //push owner the first
        var userResourcePermissionOwner = userResourcePermissionRepository.findByUserAndResource(userLoggedIn, resource)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_PERMISSION_RESOURCE_NOT_FOUND));
        userResourcePermissionOwner.getUser().setUsername(ownerResource.getUsername() + "(you)");
        userResourcePermissions.add(0, userResourcePermissionOwner);

        var userSharedDTOResponses = userResourcePermissions.stream()
                .map(user -> ResourceMapper.toUserSharedDTOResponse(user.getUser(), Constants.SHARED_RESOURCE_PERMISSION_MESSAGE))
                .peek(user -> {
                    if (Objects.equals(user.getUserShareId(), ownerResource.getId())) {
                        user.setPermission(CREATOR_RESOURCE_PERMISSION_MESSAGE);
                    }
                })
                .toList();

        return ResourceSharedDTOResponse.builder()
                .resourceId(resource.getId())
                .resourceName(resource.getName())
                .userSharedDTOResponses(userSharedDTOResponses)
                .visualType(resource.getVisualType())
                .build();
    }

    @Override
    public List<UserSharedDTOResponse> suggestionUserShare(String text) {
        User userLoggedIn = userHelper.getUserLogin();
        List<User> users = userRoleRepository.findTeacherByUsernameOrEmailContaining(text, userLoggedIn.getId());
        return users.stream()
                .map(user -> ResourceMapper.toUserSharedDTOResponse(user, Constants.SHARED_RESOURCE_PERMISSION_MESSAGE))
                .toList();
    }

    @Override
    public List<TagSuggestDTOResponse> getListTagsSuggest(TagSuggestDTORequest request) {
        return tagRepository
                .findAllByNameContainsAndActive(request.getTagSuggest()).stream()
                .map(TagMapper::toTagSuggestDTOResponse)
                .toList();
    }

    @Override
    public org.springframework.core.io.Resource getResourceIOByFilename(String filename) {
        var userLoggedIn = userHelper.getUserLogin();
        if (userLoggedIn == null || !userLoggedIn.getAvatar().equalsIgnoreCase(filename)) {
            var resourceCurrent = resourceRepository.findResourceByResourceSrcOrThumbnailSrc(filename)
                    .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_RESOURCE_NOT_FOUND));
            //check permission resource
            boolean isPermission = checkPermissionResource
                    .needCheckPermissionResource(userLoggedIn, resourceCurrent, PermissionResourceType.V);
            if (!isPermission)
                throw ApiException.forBiddenException(messageException.MSG_NO_PERMISSION);
        }
        Path root = Paths.get("documents");
        String filePath = root.resolve(filename).toString();
        org.springframework.core.io.Resource resource = new ClassPathResource(filePath);
        if (!resource.exists()) {
            throw ApiException.notFoundException(messageException.MSG_FILE_NOT_FOUND);
        }
        return resource;
    }

    @Override
    public DataMaterialsDTOResponse getFromClassToLesson(ClassLessonId classLessonId) {
        return materialsCriteria.getFromClassToLesson(classLessonId);
    }

    @Override
    public List<TagSuggestDTOResponse> getListTagsGlobalSuggest(TagSuggestDTORequest request) {
        return tagRepository
                .findGlobalTagByNameContainsAndActive(request.getTagSuggest()).stream()
                .map(TagMapper::toTagSuggestDTOResponse)
                .toList();
    }

    @Override
    public ResourceDTOUpdateResponse viewResource(Long id) {
        User user = userHelper.getUserLogin();
        var resource = resourceRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_RESOURCE_NOT_FOUND));
        boolean isPermission = checkPermissionResource
                .needCheckPermissionResource(user, resource, PermissionResourceType.V);
        if (resource.getAuthor() != user || !isPermission)
            throw ApiException.forBiddenException(messageException.MSG_NO_PERMISSION);
        return ResourceMapper.toResourceDTOUpdateResponse(resource);
    }

    @Override
    public Boolean updateResource(ResourceDTOUpdateRequest request, MultipartFile[] file) {
        User user = userHelper.getUserLogin();
        var data = new Object() {
            Resource resource = resourceRepository.findByIdAndActiveTrue(request.getId())
                    .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_RESOURCE_NOT_FOUND));
        };

        boolean isPermission = checkPermissionResource
                .needCheckPermissionResource(user, data.resource, PermissionResourceType.U);
        if (data.resource.getAuthor() != user || !isPermission)
            throw ApiException.forBiddenException(messageException.MSG_NO_PERMISSION);
        FileDTOResponse fileDTOResponse;

        if (file[0].getSize() > 0) {
            //resource temp
            Resource resourceTemp = data.resource;

            //save new resource
            if (file.length > 1) {
                throw ApiException.badRequestException("Choose only one file");
            }
            fileDTOResponse = fileService.uploadMultiFile(file, request.getLessonId()).get(0);
            data.resource.setResourceSrc(fileDTOResponse.getResourceSrc());
            data.resource.setThumbnailSrc(fileDTOResponse.getThumbnailSrc());
            data.resource.setResourceType(fileDTOResponse.getResourceType());
            data.resource.setSize(fileDTOResponse.getSize());
            data.resource.setTabResourceType(TabResourceType.findByTabResourceType(fileDTOResponse.getResourceType()));

            //delete old resource
            boolean flag = ResourceType.getFeeList().stream()
                    .anyMatch(resourceType -> resourceTemp.getResourceType() == resourceType);
            fileService.deleteFileResource(resourceTemp.getResourceSrc());
            if (flag) {
                fileService.deleteFileResource(resourceTemp.getThumbnailSrc());
            }
        }
        //delete and add resource tag
        if (request.getLessonId() == null && request.getTagList() != null) {
            List<Long> resourceTagOld = data.resource.getResourceTagList().stream()
                    .map(resourceTag -> resourceTag.getTag().getId())
                    .toList();

            Set<Long> resourceTagNew = request.getTagList();
            Set<Long> resourceTagPresent = data.resource.getResourceTagList().stream()
                    .map(resourceTag -> resourceTag.getTag().getId())
                    .collect(Collectors.toSet());
            if (resourceTagNew != resourceTagPresent) {
                List<ResourceTag> listAddResourceTags = resourceTagNew.stream()
                        .filter(resourceTag -> !resourceTagOld.contains(resourceTag))
                        .map(tagId -> ResourceTagMapper.toResourceTag(
                                tagRepository.findByIdAndActiveTrue(tagId).orElseThrow(),
                                data.resource
                        ))
                        .toList();

                List<Long> list = resourceTagOld.stream()
                        .filter(resourceTag -> !resourceTagNew.contains(resourceTag))
                        .toList();

                List<ResourceTag> listDeleteResourceTags = resourceTagOld.stream()
                        .filter(resourceTag -> !resourceTagNew.contains(resourceTag))
                        .map(tagId -> resourceTagRepository.findByTagAndResource(tagId, data.resource.getId(), true))
                        .toList();
                listDeleteResourceTags.forEach(rt -> resourceTagService.disableTagFromResource(rt.getId()));
                resourceTagRepository.deleteAll(listDeleteResourceTags);
                resourceTagRepository.saveAll(listAddResourceTags);
            }
        }

        Subject subject = subjectRepository.findByIdAndActiveTrue(request.getSubjectId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_SUBJECT_NOT_FOUND));
        Lesson lesson = request.getLessonId() == null ? null : lessonRepository.findByIdAndActiveTrue(request.getId())
                .orElse(null);
        data.resource.setSubject(subject);
        data.resource.setLesson(lesson);
        data.resource.setName(request.getName());
        data.resource.setDescription(request.getDescription());
        data.resource.setVisualType(request.getVisualType());
        data.resource = resourceRepository.save(data.resource);
        return true;
    }

    public void saveToResourceTag(ResourceTag rt) {
        if (rt != null) {
            ResourceTag resourceTag = resourceTagRepository
                    .findByTagAndResource(rt.getTag().getId(), rt.getResource().getId(), false);
            if (resourceTag != null) {
                resourceTag.setActive(true);
                resourceTagRepository.save(resourceTag);
            } else {
                resourceTagRepository.save(rt);
            }
        }
    }

}
