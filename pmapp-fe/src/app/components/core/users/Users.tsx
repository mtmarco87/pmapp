import React, { useCallback, useEffect, useState } from 'react';
import { Button, Grid, Icon, Paper } from '@material-ui/core';
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
import CreateUserDialog from '../../shared/CreateUserDialog/CreateUserDialog';

export default function Users() {
    const classes = useUsersStyles();
    const [users, setUsers] = useState<UserDto[]>([]);
    const userRole = useAppSelector(selectLoggedUser)?.role;
    const dispatch = useAppDispatch();
    const [loading, setLoading] = useState<boolean>(false);
    const [openCreate, setOpenCreate] = useState<boolean>(false);

    const loadUsers = useCallback(async () => {
        try {
            const { data } = await userService.FindAll();

            const usersWithIds = data?.map(user => ({ id: user.username, ...user }));
            setUsers(usersWithIds);
        } catch (ex) {
        }
        finally {
            setLoading(false);
        }



    }, []);

    useEffect(() => {
        setLoading(true);

        loadUsers();
    }, [loadUsers]);

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

    const handleCreateDialogOpen = useCallback(() => {
        setOpenCreate(true);
    }, []);

    const handleCreate = useCallback((user: UserDto) => {
        userService.Create(user).then((response) => {
            loadUsers();
            dispatch(setNotification({
                message: 'User ' + response?.data?.username + ' has been successfully created!',
                type: 'success'
            }));
            setOpenCreate(false);
        });
    }, [dispatch, loadUsers]);

    if (userRole !== Role.Administrator) {
        dispatch(setStatus(SessionStatus.Forbidden));
        return <Redirect to={{ pathname: '/' }} />;
    }

    return (
        <>
            <div className={classes.header}>
                <h1>Users</h1>
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
            <CreateUserDialog
                open={openCreate}
                setOpen={setOpenCreate}
                handleCreate={handleCreate}
            />
        </>
    );
}
