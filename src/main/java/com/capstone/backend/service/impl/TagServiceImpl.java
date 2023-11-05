package com.capstone.backend.service.impl;

import com.capstone.backend.entity.Tag;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.tag.PagingTagDTOResponse;
import com.capstone.backend.model.dto.tag.TagDTOFilter;
import com.capstone.backend.model.dto.tag.TagDTORequest;
import com.capstone.backend.model.dto.tag.TagDTOResponse;
import com.capstone.backend.model.mapper.TagMapper;
import com.capstone.backend.repository.TagRepository;
import com.capstone.backend.repository.criteria.TagCriteria;
import com.capstone.backend.service.TagService;
import com.capstone.backend.utils.FileHelper;
import com.capstone.backend.utils.MessageException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TagServiceImpl implements TagService {
    TagRepository tagRepository;
    TagCriteria tagCriteria;
    MessageException messageException;

    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public TagDTOResponse createTag(TagDTORequest tagDTORequest) {
        if (tagRepository.findByNameEqualsIgnoreCaseAndActiveTrue(tagDTORequest.getName()).isPresent()) {
            throw ApiException.badRequestException(messageException.MSG_TAG_EXISTED);
        }
        if (FileHelper.checkContentInputValid(tagDTORequest.getName()))
            throw ApiException.badRequestException(messageException.MSG_TEXT_NO_STANDARD_WORD);
        Tag tag = TagMapper.toTag(tagDTORequest);
        tag = tagRepository.save(tag);
        return TagMapper.toTagDTOResponse(tag);
    }

    @Override
    public TagDTOResponse disableTag(long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_TAG_NOT_FOUND));
        tag.setActive(false);
        tagRepository.save(tag);
        return TagMapper.toTagDTOResponse(tag);
    }

    @Override
    public PagingTagDTOResponse searchTags(TagDTOFilter tagDTOFilter) {
        return tagCriteria.searchTag(tagDTOFilter);
    }

    @Override
    public TagDTOResponse getTagByID(long id) {
        Tag tag = tagRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_TAG_NOT_FOUND));
        return TagMapper.toTagDTOResponse(tag);
    }
}
