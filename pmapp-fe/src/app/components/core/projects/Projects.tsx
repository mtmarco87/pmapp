import React, { useCallback, useEffect, useState } from 'react';
import { Button, Grid, Icon, Paper } from '@material-ui/core';
import useProjectsStyles from './Projects.styles';
import { DataGrid } from '@material-ui/data-grid';
import { projectService } from '../../../services/projectService';
import { ProjectDto } from '../../../models/dtos/ProjectDto';
import { getProjectsColumnsDefs } from './getProjectsColumnsDef';
import { useAppDispatch, useAppSelector } from '../../../redux/hooks';
import { selectLoggedUser, setNotification } from '../../../redux/slices/sessionSlice';
import { useHistory } from 'react-router';
import { Role } from '../../../models/dtos/Role';
import { AxiosResponse } from 'axios';
import { UserDto } from '../../../models/dtos/UserDto';
import { userService } from '../../../services/userService';
import { handleCellEditWithDbUpdate } from '../../../utils/Utils';
import CreateProjectDialog from '../../shared/CreateProjectDialog/CreateProjectDialog';


export default function Projects() {
    const classes = useProjectsStyles();
    const history = useHistory();
    const [projects, setProjects] = useState<ProjectDto[]>([]);
    const [users, setUsers] = useState<UserDto[]>([]);
    const loggedUser = useAppSelector(selectLoggedUser);
    const userRole = loggedUser?.role;
    const dispatch = useAppDispatch();
    const [loading, setLoading] = useState<boolean>(false);
    const [openCreate, setOpenCreate] = useState<boolean>(false);

    const loadProjects = useCallback(() => {
        let projectsRetrieveFn: Promise<AxiosResponse<ProjectDto[]>>;
        if (userRole === Role.Administrator) {
            projectsRetrieveFn = projectService.FindAll();
        }
        else {
            projectsRetrieveFn = projectService.Me();
        }

        projectsRetrieveFn.then((response) => {
            const projectsWithIds = response?.data?.map(proj => ({ id: proj.code, ...proj }));
            setProjects(projectsWithIds);
        }).finally(() => setLoading(false));
    }, [userRole]);

    const loadUsers = useCallback(() => {
        setLoading(true);

        userService.FindAll().then((response) => {
            setUsers(response?.data);

            loadProjects();
        });
    }, [loadProjects]);

    useEffect(() => {
        loadUsers();
    }, [loadUsers]);

    const deleteProject = useCallback((code: number) => {
        projectService.DeleteByCode(code)
            .then(() => {
                loadProjects();
                dispatch(setNotification({
                    message: 'Project ' + code + ' has been successfully deleted!',
                    type: 'success'
                }));
            });
    }, [dispatch, loadProjects]);

    const navigateToProject = useCallback((code: number) => {
        history.push("/projects/" + code);
    }, [history]);

    // eslint-disable-next-line
    const handleEditCellChangeCommitted = useCallback(
        handleCellEditWithDbUpdate<ProjectDto>(
            projects,
            setProjects,
            'code',
            projectService.Update,
            dispatch,
            () => loadProjects(),
            'Project %s has been successfully updated!'
        ), [projects, loadProjects]);

    const handleCreateDialogOpen = useCallback(() => {
        setOpenCreate(true);
    }, []);

    const handleCreate = useCallback((project: ProjectDto) => {
        projectService.Create(project).then((response) => {
            loadProjects();
            dispatch(setNotification({
                message: 'Project ' + response?.data?.code + ' has been successfully created!',
                type: 'success'
            }));
            setOpenCreate(false);
        });
    }, [dispatch, loadProjects]);

    return (
        <>
            <div className={classes.header}>
                <h1>Projects</h1>
                {userRole === Role.Administrator &&
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
            {userRole === Role.Administrator ?
                <span>Here you will find the list of all projects.</span>
                :
                <span>Here you will find the list of your projects.</span>
            }
            <br />
            <br />

            <Grid container spacing={3}>
                {/* Chart */}
                <Grid item xs={12} md={12} lg={12}>
                    <Paper className={classes.paper}>
                        <div style={{ height: 500, minHeight: 500, width: '100%' }}>
                            <DataGrid
                                rows={projects}
                                columns={getProjectsColumnsDefs({
                                    classes,
                                    deleteProject,
                                    navigateToProject,
                                    userRole,
                                    users
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
                            ></DataGrid>
                        </div>
                    </Paper>
                </Grid>
            </Grid>
            <CreateProjectDialog
                open={openCreate}
                setOpen={setOpenCreate}
                handleCreate={handleCreate}
                loggedUser={loggedUser}
                users={users}
            />
        </>
    );
}
