package com.capstone.backend.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:messages_exception.properties")
public class MessageException {

    //-------------- user --------------
    @Value("${messages.exception.user-not-found}")
    public String MSG_USER_NOT_FOUND;

    @Value("${messages.exception.user-unauthorized}")
    public String MSG_USER_UNAUTHORIZED;

    @Value("${messages.exception.user-email-existed}")
    public String MSG_USER_EMAIL_EXISTED;

    @Value("${messages.exception.user-email-confirmed}")
    public String MSG_USER_EMAIL_CONFIRMED;

    @Value("${messages.exception.user-username-existed}")
    public String MSG_USER_USERNAME_EXISTED;

    @Value("${messages.exception.user-phone-existed}")
    public String MSG_USER_PHONE_EXISTED;

    @Value("${messages.exception.user-wrong-password}")
    public String MSG_USER_WRONG_PASSWORD;

    @Value("${messages.exception.user-not-match-password}")
    public String MSG_USER_NOT_MATCH_PASSWORD;

    //-------------- role --------------
    @Value("${messages.exception.role-not-found}")
    public String MSG_ROLE_NOT_FOUND;

    //-------------- resource --------------
    @Value("${messages.exception.resource-not-found}")
    public String MSG_RESOURCE_NOT_FOUND;

    //-------------- comment --------------
    @Value("${messages.exception.comment-root-id-not-found}")
    public String MSG_COMMENT_ROOT_ID_NOT_FOUND;

    @Value("${messages.exception.comment-not-found}")
    public String MSG_COMMENT_NOT_FOUND;

    @Value("${messages.exception.text-not-standard-word}")
    public String MSG_TEXT_NO_STANDARD_WORD;

    //-------------- token --------------
    @Value("${messages.exception.token-not-found}")
    public String MSG_TOKEN_NOT_FOUND;

    @Value("${messages.exception.token-expired}")
    public String MSG_TOKEN_EXPIRED;

    @Value("${messages.exception.token-invalid}")
    public String MSG_TOKEN_INVALID;

    //-------------- file --------------
    @Value("${messages.exception.folder-create-error}")
    public String MSG_FOLDER_CREATE_ERROR;

    @Value("${messages.exception.file-type-invalid}")
    public String MSG_FILE_TYPE_INVALID;

    @Value("${messages.exception.file-save-error}")
    public String MSG_FILE_SAVE_ERROR;

    @Value("${messages.exception.file-download-error}")
    public String MSG_FILE_DOWNLOAD_ERROR;

    @Value("${messages.exception.file-too-large}")
    public String MSG_FILE_TOO_LARGE;

    @Value("${messages.exception.file-convert-error}")
    public String MSG_FILE_CONVERT_ERROR;

    @Value("${messages.exception.file-not-found}")
    public String MSG_FILE_NOT_FOUND;

    //-------------- report comment --------------
    @Value("${messages.exception.report-comment-reported}")
    public String MSG_REPORT_COMMENT_REPORTED;

    //-------------- report resource --------------
    @Value("${messages.exception.report-resource-reported}")
    public String MSG_REPORT_RESOURCE_REPORTED;

    //-------------- class --------------
    @Value("${messages.exception.class-not-found}")
    public String MSG_CLASS_NOT_FOUND;

    //-------------- book series --------------
    @Value("${messages.exception.book-series-not-found}")
    public String MSG_BOOK_SERIES_NOT_FOUND;

    //-------------- subject --------------
    @Value("${messages.exception.subject-not-found}")
    public String MSG_SUBJECT_NOT_FOUND;

    //-------------- book volume --------------
    @Value("${messages.exception.book-volume-not-found}")
    public String MSG_BOOK_VOLUME_NOT_FOUND;

    //-------------- chapter --------------
    @Value("${messages.exception.chapter-not-found}")
    public String MSG_CHAPTER_NOT_FOUND;

    //-------------- lesson --------------
    @Value("${messages.exception.lesson-not-found}")
    public String MSG_LESSON_NOT_FOUND;

    //-------------- tag --------------
    @Value("${messages.exception.tag-not-found}")
    public String MSG_TAG_NOT_FOUND;

    @Value("${messages.exception.tag-existed}")
    public String MSG_TAG_EXISTED;

    @Value("${messages.exception.tag-applied}")
    public String MSG_TAG_APPLIED;

    //-------------- server --------------
    @Value("${messages.exception.internal-server-error}")
    public String MSG_INTERNAL_SERVER_ERROR;

    @Value("${messages.exception.sending-mail}")
    public String MSG_ERROR_SENDING_MAIL;

    @Value("${messages.exception.bearer-not-found}")
    public String MSG_BEARER_NOT_FOUND;

    @Value("${messages.exception.no-permission}")
    public String MSG_NO_PERMISSION;

    //-------------- system permission --------------
    @Value("${messages.exception.system-permission-not-found}")
    public String MSG_SYSTEM_PERMISSION_NOT_FOUND;

    @Value("${messages.exception.user-permission-resource-not-found}")
    public String MSG_PERMISSION_RESOURCE_NOT_FOUND;

    @Value("${messages.exception.user-resource-not-found}")
    public String MSG_USER_RESOURCE_NOT_FOUND;

}
