package com.ricardo.pmapp.utils;

import com.ricardo.pmapp.persistence.models.entities.Project;
import com.ricardo.pmapp.persistence.models.entities.Task;
import com.ricardo.pmapp.persistence.models.enums.Role;
import com.ricardo.pmapp.security.models.UserPrincipal;

public class PermissionUtils {

    public static boolean canModifyProject(Project project, UserPrincipal requester) {
        return requester.getRole() == Role.Administrator ||
                (requester.getRole() == Role.ProjectManager &&
                        requester.getUsername().equals(project.getProjectManager().getUsername()));
    }


    public static boolean canModifyTask(Task task, UserPrincipal requester) {
        return task.getProject() == null ||
                requester.getRole() == Role.Administrator ||
                (requester.getRole() == Role.ProjectManager &&
                        requester.getUsername().equals(task.getProject().getProjectManager().getUsername()));
    }
}
