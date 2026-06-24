package com.frame.api.dashboard.dto;

import java.util.List;

public record DashboardSummaryResponse(

        long totalWorkspaces,
        long totalProjects,
        long activeProjects,
        long archivedProjects,
        long totalScenes,
        long scenesInProgress,
        long scenesInReview,
        long approvedScenes,
        List<DashboardRecentSceneResponse> recentScenes
) {
}
