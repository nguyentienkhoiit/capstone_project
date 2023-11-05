package com.capstone.backend.service;

import com.capstone.backend.model.dto.materials.DataMaterialsDTOResponse;
import com.capstone.backend.model.dto.materials.MaterialsFilterDTORequest;
import com.capstone.backend.model.dto.resource.*;
import com.capstone.backend.model.dto.tag.TagSuggestDTORequest;
import com.capstone.backend.model.dto.tag.TagSuggestDTOResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResourceService {
    public List<ResourceDTOResponse> uploadResource(ResourceDTORequest request, MultipartFile[] files);

    public ResourceDetailDTOResponse getResourceDetailById(Long id);

    public PagingResourceDTOResponse searchMediaResource(ResourceMediaDTOFilter resourceDTOFilter);

    public DataMaterialsDTOResponse searchMaterials(MaterialsFilterDTORequest request);

    public Resource downloadResource(String fileName);

    public Boolean shareResource(ResourceSharedDTORequest request);

    public ResourceSharedDTOResponse viewResourceShareById(Long resourceId);

    public List<UserSharedDTOResponse> suggestionUserShare(String text);

    public List<TagSuggestDTOResponse> getListTagsSuggest(TagSuggestDTORequest request);

    public Resource getResourceIOByFilename(String filename);

    public DataMaterialsDTOResponse getFromClassToLesson(ClassLessonId classLessonId);

    public List<TagSuggestDTOResponse> getListTagsGlobalSuggest(TagSuggestDTORequest request);

    public ResourceDTOUpdateResponse viewResource(Long id);

    public Boolean updateResource(ResourceDTOUpdateRequest request, MultipartFile[] file);
}
