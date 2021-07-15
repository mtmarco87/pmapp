import React, { useCallback, useEffect, useState } from 'react';
import { Grid, Paper } from '@material-ui/core';
import { DataGrid } from '@material-ui/data-grid';
import useUsersStyles from './Users.styles';
import { userService } from '../../../services/userService';
import { UserDto } from '../../../models/dtos/UserDto';
import { useAppDispatch, useAppSelector } from '../../../redux/hooks';
import { selectLoggedUser, setNotification, setStatus } from '../../../redux/slices/sessionSlice';
import { SessionStatus } from '../../../models/core/SessionStatus';
import { Redirect } from 'react-router';
import { Role } from '../../../models/dtos/Role';
import { getUsersColumnsDefs } from './getUsersColumnsDef';
import { handleCellEditWithDbUpdate } from '../../../utils/Utils';

export default function Users() {
    const classes = useUsersStyles();
    const [users, setUsers] = useState<UserDto[]>([]);
    const userRole = useAppSelector(selectLoggedUser)?.role;
    const dispatch = useAppDispatch();
    const [loading, setLoading] = useState<boolean>(false);


    const loadUsers = useCallback(() => {
        userService.FindAll().then((response) => {
            const usersWithIds = response?.data?.map(user => ({ id: user.username, ...user }));
            setUsers(usersWithIds);
        }).finally(() => setLoading(false));
    }, []);

    useEffect(() => {
        setLoading(true);

        loadUsers();
    }, []);

    const deleteUser = useCallback((username: string) => {
        userService.DeleteByUsername(username)
            .then(() => {
                loadUsers();
                dispatch(setNotification({
                    message: 'User ' + username + ' has been successfully deleted!',
                    type: 'success'
                }));
            });
    }, [loadUsers, dispatch]);

    if (userRole !== Role.Administrator) {
        dispatch(setStatus(SessionStatus.Forbidden));
        return <Redirect to={{ pathname: '/' }} />;
    }

    // eslint-disable-next-line
    const handleEditCellChangeCommitted = useCallback(
        handleCellEditWithDbUpdate<UserDto>(
            users,
            setUsers,
            'username',
            userService.Update,
            dispatch,
            () => loadUsers(),
            'User %s has been successfully updated!'
        ), [users, loadUsers]);

    return (
        <>
            <h1>Users</h1>
            <span>Users management</span>
            <br />
            <br />

            <Grid container spacing={3}>
                {/* Chart */}
                <Grid item xs={12} md={12} lg={12}>
                    <Paper className={classes.paper}>
                        <div style={{ height: 400, width: '100%' }}>
                            <DataGrid
                                rows={users}
                                columns={getUsersColumnsDefs({ classes, deleteUser })}
                                disableSelectionOnClick
                                onEditCellChangeCommitted={handleEditCellChangeCommitted}
                                sortModel={[
                                    {
                                        field: 'surname',
                                        sort: 'asc'
                                    },
                                    {
                                        field: 'name',
                                        sort: 'asc'
                                    },
                                ]}
                                loading={loading}
                            />
                        </div>
                    </Paper>
                </Grid>
            </Grid>
        </>
    );
}
