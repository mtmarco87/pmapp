package com.ricardo.pmapp.utils;

import com.ricardo.pmapp.persistence.models.entities.Project;
import com.ricardo.pmapp.persistence.models.entities.Task;
import com.ricardo.pmapp.persistence.models.enums.Role;
import com.ricardo.pmapp.security.models.UserPrincipal;

public class PermissionUtils {

    public static boolean canModifyProject(Project project, UserPrincipal requester) {
        return project == null ||
                requester.getRole() == Role.Administrator ||
                (requester.getRole() == Role.ProjectManager &&
                        requester.getUsername().equals(project.getProjectManager().getUsername()));
    }

    public static boolean canModifyTask(Task task, Project project, UserPrincipal requester) {
        return canModifyProject(project, requester) ||
                task.getAssignee() == null ||
                ((requester.getRole() == Role.ProjectManager || requester.getRole() == Role.Developer) &&
                        requester.getUsername().equals(task.getAssignee().getUsername()));
    }
}
