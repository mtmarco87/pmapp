import React, { useCallback, useEffect, useState } from 'react';
import { Button, Grid, Icon, Paper } from '@material-ui/core';
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
import { handleCellEditWithDbUpdate } from '../../../utils/Utils';
import { Role } from '../../../models/dtos/Role';
import CreateTaskDialog from '../../shared/CreateTaskDialog/CreateTaskDialog';


export default function Tasks() {
    const classes = useTasksStyles();
    const [tasks, setTasks] = useState<TaskDto[]>([]);
    const [projects, setProjects] = useState<ProjectDto[]>([]);
    const [users, setUsers] = useState<UserDto[]>([]);
    const loggedUser = useAppSelector(selectLoggedUser);
    const dispatch = useAppDispatch();
    const [loading, setLoading] = useState<boolean>(false);
    const [openCreate, setOpenCreate] = useState<boolean>(false);

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
        })).finally(() => setLoading(false));
    }, []);

    const loadUsersAndProjects = useCallback(() => {
        setLoading(true);

        axios.all<AxiosResponse<any>>([
            userService.FindAll(),
            projectService.FindAll(),
        ]).then(axios.spread((...responses) => {
            const allUsers = (responses[0] as AxiosResponse<UserDto[]>)?.data;
            const allProjects = (responses[1] as AxiosResponse<ProjectDto[]>)?.data;
            setUsers(allUsers);
            setProjects(allProjects);

            loadUserTasks();
        }));
    }, [loadUserTasks]);

    useEffect(() => {
        loadUsersAndProjects();
    }, [loadUsersAndProjects])

    const deleteTask = useCallback((code: number) => {
        taskService.DeleteByCode(code)
            .then(() => {
                loadUserTasks();
                dispatch(setNotification({
                    message: 'Task ' + code + ' has been successfully deleted!',
                    type: 'success'
                }));
            });
    }, [loadUserTasks, dispatch]);

    // eslint-disable-next-line
    const handleEditCellChangeCommitted = useCallback(
        handleCellEditWithDbUpdate<TaskDto>(
            tasks,
            setTasks,
            'code',
            taskService.Update,
            dispatch,
            () => loadUserTasks(),
            'Task %s has been successfully updated!'
        ), [tasks, loadUserTasks]);

    const handleCreateDialogOpen = useCallback(() => {
        setOpenCreate(true);
    }, []);

    const handleCreate = useCallback((task: TaskDto) => {
        taskService.Create(task).then((response) => {
            loadUserTasks();
            dispatch(setNotification({
                message: 'Task ' + response?.data?.code + ' has been successfully created!',
                type: 'success'
            }));
            setOpenCreate(false);
        });
    }, [dispatch, loadUserTasks]);

    return (
        <>
            <div className={classes.header}>
                <h1>Your Tasks</h1>
                {(loggedUser?.role === Role.Administrator || loggedUser?.role === Role.ProjectManager) &&
                    <Button
                        variant="contained"
                        color="primary"
                        className={classes.button}
                        endIcon={<Icon>add</Icon>}
                        onClick={handleCreateDialogOpen}
                    >
                        Create
                    </Button>
                }
            </div>
            <span>Here you will find the list of your tasks and the unassigned tasks.</span>
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
                                    classes, deleteTask, loggedUser, users, projects
                                })}
                                disableSelectionOnClick
                                onEditCellChangeCommitted={handleEditCellChangeCommitted}
                                sortModel={[
                                    {
                                        field: 'code',
                                        sort: 'asc'
                                    },
                                ]}
                                loading={loading}
                            />
                        </div>
                    </Paper>
                </Grid>
            </Grid>
            <CreateTaskDialog
                open={openCreate}
                setOpen={setOpenCreate}
                handleCreate={handleCreate}
                loggedUser={loggedUser}
                users={users}
                projects={projects}
            />
        </>
    );
}
