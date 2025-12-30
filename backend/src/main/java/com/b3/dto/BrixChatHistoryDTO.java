package com.b3.dto;

import java.util.List;

/**
 * DTO for displaying BRIX chat history
 * Used when loading chat conversation (GET /brix/chat-history)
 */
public class BrixChatHistoryDTO {

    private Long profileId;
    private String userName;
    private Integer totalMessages;
    private String oldestMessageDate; // ISO format
    private String newestMessageDate; // ISO format
    
    // List of messages (newest first)
    private List<BrixMessageDTO> messages;
    
    // Pagination info
    private Integer page;
    private Integer pageSize;
    private Boolean hasMore; // Are there more messages to load?

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public BrixChatHistoryDTO() {}

    public BrixChatHistoryDTO(Long profileId, String userName, Integer totalMessages,
                             String oldestMessageDate, String newestMessageDate,
                             List<BrixMessageDTO> messages, Integer page,
                             Integer pageSize, Boolean hasMore) {
        this.profileId = profileId;
        this.userName = userName;
        this.totalMessages = totalMessages;
        this.oldestMessageDate = oldestMessageDate;
        this.newestMessageDate = newestMessageDate;
        this.messages = messages;
        this.page = page;
        this.pageSize = pageSize;
        this.hasMore = hasMore;
    }

    // ========================================================================
    // GETTERS AND SETTERS
    // ========================================================================

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getTotalMessages() {
        return totalMessages;
    }

    public void setTotalMessages(Integer totalMessages) {
        this.totalMessages = totalMessages;
    }

    public String getOldestMessageDate() {
        return oldestMessageDate;
    }

    public void setOldestMessageDate(String oldestMessageDate) {
        this.oldestMessageDate = oldestMessageDate;
    }

    public String getNewestMessageDate() {
        return newestMessageDate;
    }

    public void setNewestMessageDate(String newestMessageDate) {
        this.newestMessageDate = newestMessageDate;
    }

    public List<BrixMessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<BrixMessageDTO> messages) {
        this.messages = messages;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    // ========================================================================
    // TOSTRING
    // ========================================================================

    @Override
    public String toString() {
        return "BrixChatHistoryDTO{" +
                "profileId=" + profileId +
                ", userName='" + userName + '\'' +
                ", totalMessages=" + totalMessages +
                ", messageCount=" + (messages != null ? messages.size() : 0) +
                ", page=" + page +
                ", hasMore=" + hasMore +
                '}';
    }
}
