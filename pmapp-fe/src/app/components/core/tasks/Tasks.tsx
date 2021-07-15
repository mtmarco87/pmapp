import React, { useCallback, useEffect, useState } from 'react';
import { Grid, Paper } from '@material-ui/core';
import useTasksStyles from './Tasks.styles';
import { DataGrid } from '@material-ui/data-grid';
import { getTasksColumnsDefs } from './getTasksColumnsDef';
import { useAppDispatch, useAppSelector } from '../../../redux/hooks';
import { selectLoggedUser, setNotification } from '../../../redux/slices/sessionSlice';
import { taskService } from '../../../services/taskService';
import { TaskDto } from '../../../models/dtos/TaskDto';
import axios, { AxiosResponse } from 'axios';
import { projectService } from '../../../services/projectService';
import { userService } from '../../../services/userService';
import { ProjectDto } from '../../../models/dtos/ProjectDto';
import { UserDto } from '../../../models/dtos/UserDto';


export default function Tasks() {
    const classes = useTasksStyles();
    const [tasks, setTasks] = useState<TaskDto[]>([]);
    const [projects, setProjects] = useState<ProjectDto[]>([]);
    const [users, setUsers] = useState<UserDto[]>([]);
    const userRole = useAppSelector(selectLoggedUser)?.role;
    const dispatch = useAppDispatch();

    const loadUserTasks = useCallback(() => {
        axios.all([
            taskService.Me(),
            taskService.FindNotAssigned(),
        ]).then(axios.spread((...responses) => {
            const currentUserTasksResponse = responses[0] as AxiosResponse<TaskDto[]>;
            const unassignedTasksResponse = responses[1] as AxiosResponse<TaskDto[]>;

            const currentUserTasksWithIds =
                currentUserTasksResponse?.data?.map(task => ({ id: task.code, ...task }));
            const unassignedTasksWithIds =
                unassignedTasksResponse?.data?.map(task => ({ id: task.code, ...task }));
            const result = currentUserTasksWithIds.concat(unassignedTasksWithIds);
            setTasks(result);
        }));
    }, []);

    const loadUsersAndProjects = useCallback(() => {
        axios.all<AxiosResponse<any>>([
            userService.FindAll(),
            projectService.FindAll(),
        ]).then(axios.spread((...responses) => {
            const allUsers = (responses[0] as AxiosResponse<UserDto[]>)?.data;
            const allProjects = (responses[1] as AxiosResponse<ProjectDto[]>)?.data;
            setUsers(allUsers);
            setProjects(allProjects);
        }));
    }, []);

    useEffect(() => {
        loadUsersAndProjects();
        loadUserTasks();
    }, [loadUsersAndProjects, loadUserTasks])

    const deleteTask = (code: number) => {
        taskService.DeleteByCode(code)
            .then(() => {
                loadUserTasks();
                dispatch(setNotification({
                    message: 'Task ' + code + ' has been successfully deleted!',
                    type: 'success'
                }));
            });
    };

    return (
        <>
            <h1>Your Tasks</h1>
            <span>Here you will find the list of your tasks.</span>
            <br />
            <br />

            <Grid container spacing={3}>
                {/* Chart */}
                <Grid item xs={12} md={12} lg={12}>
                    <Paper className={classes.paper}>
                        <div style={{ height: 500, minHeight: 500, width: '100%' }}>
                            <DataGrid
                                rows={tasks}
                                columns={getTasksColumnsDefs({
                                    classes, deleteTask, userRole, users, projects
                                })}
                                autoPageSize={true}
                                disableSelectionOnClick
                                loading={!tasks || !projects || !users}
                            />
                        </div>
                    </Paper>
                </Grid>
            </Grid>
        </>
    );
}
