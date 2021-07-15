import { IconButton } from '@material-ui/core';
import { GridColDef } from '@material-ui/data-grid';
import DeleteIcon from '@material-ui/icons/Delete';
import moment from 'moment';
import { ProjectDto } from '../../../models/dtos/ProjectDto';
import { Role } from '../../../models/dtos/Role';
import { TaskStatus } from '../../../models/dtos/TaskStatus';
import { UserDto } from '../../../models/dtos/UserDto';
import { DataGridSelectField } from '../../shared/DatagridSelectField/DatagridSelectField';

export const getTasksColumnsDefs = ({ classes, deleteTask, userRole, users, projects }:
    { classes: any, deleteTask: Function, userRole: Role | undefined, users: UserDto[], projects: ProjectDto[] }) => {
    const columns: GridColDef[] = (
        [
            {
                field: 'code',
                headerName: 'Code',
                width: 120,
                type: 'number'
            },
            {
                field: 'assignee',
                headerName: 'Assignee',
                width: 200,
                editable: userRole === Role.Administrator || userRole === Role.ProjectManager,
                valueFormatter: (params) => {
                    const projectManager = users?.find(u => u.username === params.value);
                    return !!projectManager ?
                        `${projectManager?.name} ${projectManager?.surname}`
                        :
                        'N/A';
                },
                renderEditCell: (params) => {
                    return DataGridSelectField(
                        {
                            options: userRole === Role.Administrator ?
                                users
                                :
                                users.filter(usr => usr.role !== Role.Administrator),
                            idField: 'username',
                            labelRenderFn: (user: UserDto) => {
                                return `${user?.name} ${user?.surname}`
                            }
                        },
                        params
                    );
                }
            },
            {
                field: 'project',
                headerName: 'Project',
                width: 200,
                editable: userRole === Role.Administrator,
                valueFormatter: (params) => {
                    return projects?.find(p => p.code === params.value)?.name;
                },
                renderEditCell: (params) => {
                    return DataGridSelectField(
                        {
                            options: projects,
                            idField: 'code',
                            labelRenderFn: (proj: ProjectDto) => {
                                return proj?.name;
                            }
                        },
                        params
                    );
                }
            },
            {
                field: 'description',
                headerName: 'Description',
                width: 350,
                editable: true,
            },
            {
                field: 'progress',
                headerName: 'Progress',
                width: 150,
                editable: true,
                type: 'number',
            },
            {
                field: 'status',
                headerName: 'Status',
                width: 130,
                editable: true,
                renderEditCell: (params) => {
                    return DataGridSelectField(
                        {
                            options: [TaskStatus.New, TaskStatus.InProgress, TaskStatus.Completed]
                        },
                        params
                    );
                }
            },
            {
                field: 'deadline',
                headerName: 'Deadline',
                width: 150,
                editable: userRole === Role.Administrator || userRole === Role.ProjectManager,
                type: 'date',
                valueGetter: (params) => {
                    const dateStr: string = params?.value as string;
                    const date = moment(dateStr).format('YYYY-MM-DD');
                    return date.toString();
                },
            },
            {
                field: '',
                headerName: 'Actions',
                width: 140,
                renderCell: (params) => {
                    return (
                        <>
                            {userRole !== Role.Developer &&
                                < div className={classes.actionButtons} >
                                    <IconButton
                                        edge='start'
                                        color='inherit'
                                        aria-label='delete task'
                                        title='Delete task'
                                        onClick={() => deleteTask(params.id)}>
                                        <DeleteIcon />
                                    </IconButton>
                                </ div>
                            }
                        </>
                    );
                }
            }
        ]
    );

    return columns;
}
