import React, { useCallback, useEffect, useState } from 'react';
import { Button, Grid, Icon, Paper, Typography } from '@material-ui/core';
import useProjectStyles from './Project.styles';
import { DataGrid } from '@material-ui/data-grid';
import { projectService } from '../../../../services/projectService';
import { ProjectDto } from '../../../../models/dtos/ProjectDto';
import { getTasksColumnsDefs } from '../../tasks/getTasksColumnsDef';
import { useAppDispatch, useAppSelector } from '../../../../redux/hooks';
import { selectLoggedUser, setNotification } from '../../../../redux/slices/sessionSlice';
import { Redirect, useParams } from 'react-router';
import { taskService } from '../../../../services/taskService';
import { TaskDto } from '../../../../models/dtos/TaskDto';
import { handleCellEditWithDbUpdate, isNotFound } from '../../../../utils/Utils';
import axios, { AxiosResponse } from 'axios';
import { userService } from '../../../../services/userService';
import { UserDto } from '../../../../models/dtos/UserDto';
import CreateTaskDialog from '../../../shared/CreateTaskDialog/CreateTaskDialog';


export default function Project() {
    const classes = useProjectStyles();
    const { code } = useParams<{ code: string }>();
    const [tasks, setTasks] = useState<TaskDto[]>([]);
    const [project, setProject] = useState<ProjectDto | null>(null);
    const [projects, setProjects] = useState<ProjectDto[]>([]);
    const [users, setUsers] = useState<UserDto[]>([]);
    const [redirect, setRedirect] = useState<boolean>(false);
    const loggedUser = useAppSelector(selectLoggedUser);
    const dispatch = useAppDispatch();
    const [loading, setLoading] = useState<boolean>(false);
    const [openCreate, setOpenCreate] = useState<boolean>(false);

    const loadProjectTasks = useCallback(() => {
        taskService.FindByProject(+code).then((response) => {
            const tasksWithIds = response?.data?.map(task => ({ id: task.code, ...task }));
            setTasks(tasksWithIds);
        }).catch((error) => {
            if (isNotFound(error)) {
                setRedirect(true);
            }
        }).finally(() => setLoading(false));
    }, [code]);

    const loadUsersAndProjects = useCallback(() => {
        setLoading(true);

        axios.all<AxiosResponse<any>>([
            userService.FindAll(),
            projectService.FindAll(),
        ]).then(axios.spread((...responses) => {
            const allUsers = responses[0] as AxiosResponse<UserDto[]>;
            const allProjects = responses[1] as AxiosResponse<ProjectDto[]>;
            const currProject = allProjects?.data.find(proj => proj.code === +code);
            setUsers(allUsers?.data);
            setProjects(allProjects?.data);
            setProject(currProject ?? null);

            loadProjectTasks();
        }));
    }, [code, loadProjectTasks]);

    useEffect(() => {
        loadUsersAndProjects();
    }, [loadUsersAndProjects])

    const deleteTask = useCallback((code: number) => {
        taskService.DeleteByCode(code)
            .then(() => {
                loadProjectTasks();
                dispatch(setNotification({
                    message: 'Task ' + code + ' has been successfully deleted!',
                    type: 'success'
                }));
            });
    }, [loadProjectTasks, dispatch]);

    // eslint-disable-next-line
    const handleEditCellChangeCommitted = useCallback(
        handleCellEditWithDbUpdate<TaskDto>(
            tasks,
            setTasks,
            'code',
            taskService.Update,
            dispatch,
            () => loadProjectTasks(),
            'Task %s has been successfully updated!'
        ), [tasks, loadProjectTasks]);

    const handleCreateDialogOpen = useCallback(() => {
        setOpenCreate(true);
    }, []);

    const handleCreate = useCallback((task: TaskDto) => {
        taskService.Create(task).then((response) => {
            loadProjectTasks();
            dispatch(setNotification({
                message: 'Task ' + response?.data?.code + ' has been successfully created!',
                type: 'success'
            }));
            setOpenCreate(false);
        });
    }, [dispatch, loadProjectTasks]);

    if (redirect) {
        return <Redirect to={{ pathname: '/' }} />;
    }

    return (
        <>
            <div className={classes.header}>
                <h1>Project {code}</h1>
                <Button
                    variant="contained"
                    color="primary"
                    className={classes.button}
                    endIcon={<Icon>add</Icon>}
                    onClick={handleCreateDialogOpen}
                >
                    Create
                </Button>
            </div>

            <Typography color="inherit" noWrap>
                <b>Name:</b> {project?.name} / <b>Project Manager:</b> {project?.projectManager}
            </Typography>
            <br />
            <br />

            <Typography component="h1" variant="h6" color="inherit" noWrap>
                <b>Tasks:</b>
            </Typography>
            <Grid container spacing={3}>
                {/* Chart */}
                <Grid item xs={12} md={12} lg={12}>
                    <Paper className={classes.paper}>
                        <div style={{ height: 500, minHeight: 500, width: '100%' }}>
                            <DataGrid
                                rows={tasks}
                                columns={getTasksColumnsDefs({
                                    classes, deleteTask, loggedUser, users, currentProjectCode: +code, projects
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
                currentProjectCode={+code}
                projects={projects}
            />
        </>
    );
}
