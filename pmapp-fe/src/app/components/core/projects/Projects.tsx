import React, { useCallback, useEffect, useState } from 'react';
import { Grid, Paper } from '@material-ui/core';
import useProjectsStyles from './Projects.styles';
import { DataGrid, GridEditCellPropsParams } from '@material-ui/data-grid';
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


export default function Projects() {
    const classes = useProjectsStyles();
    const history = useHistory();
    const [projects, setProjects] = useState<ProjectDto[]>([]);
    const [users, setUsers] = useState<UserDto[]>([]);
    const userRole = useAppSelector(selectLoggedUser)?.role;
    const dispatch = useAppDispatch();

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
        });
    }, [userRole]);

    const loadUsers = useCallback(() => {
        userService.FindAll().then((response) => {
            setUsers(response?.data);

            loadProjects();
        });
    }, [loadProjects]);

    useEffect(() => {
        loadUsers();
    }, [loadUsers]);

    const deleteProject = (code: number) => {
        projectService.DeleteByCode(code)
            .then(() => {
                loadProjects();
                dispatch(setNotification({
                    message: 'Project ' + code + ' has been successfully deleted!',
                    type: 'success'
                }));
            });
    };

    // const handleEditCellChangeCommitted = useCallback(
    //     ({ id, field, props }: GridEditCellPropsParams) => {
    //       if (field === 'projectManager') {
    //         const [firstName, lastName] = props.value!.toString().split(' ');
    //         const updatedRows = rows.map((row) => {
    //           if (row.id === id) {
    //             return { ...row, firstName, lastName };
    //           }
    //           return row;
    //         });
    //         setRows(updatedRows);
    //       }
    //     },
    //     [rows],

    //   );

    return (
        <>
            <h1>Projects</h1>
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
                                columns={getProjectsColumnsDefs({ classes, deleteProject, history, users })}
                                autoPageSize={true}
                                disableSelectionOnClick
                                loading={!projects || !users}
                            ></DataGrid>
                        </div>
                    </Paper>
                </Grid>
            </Grid>
        </>
    );
}
