import { IconButton } from '@material-ui/core';
import { GridColDef } from '@material-ui/data-grid';
import OpenInBrowserIcon from '@material-ui/icons/OpenInBrowser';
import DeleteIcon from '@material-ui/icons/Delete';
import { UserDto } from '../../../models/dtos/UserDto';
import { Role } from '../../../models/dtos/Role';
import { DataGridSelectField } from '../../shared/DatagridSelectField/DatagridSelectField';


export const getProjectsColumnsDefs = ({ classes, deleteProject, navigateToProject, users }:
    { classes: any, deleteProject: Function, navigateToProject: Function, users: UserDto[] }) => {
    const columns: GridColDef[] = (
        [
            {
                field: 'code',
                headerName: 'Code',
                width: 120,
                type: 'number'
            },
            {
                field: 'name',
                headerName: 'Name',
                width: 587,
                editable: true,
            },
            {
                field: 'projectManager',
                headerName: 'Project Manager',
                width: 587,
                editable: true,
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
                            options: users.filter(
                                usr => usr.role === Role.Administrator || usr.role === Role.ProjectManager
                            ),
                            idField: 'username',
                            labelRenderFn: (user: UserDto) => {
                                return `${user?.name} ${user?.surname}`
                            }
                        },
                        params
                    );
                },
            },
            {
                field: '',
                headerName: 'Actions',
                width: 150,
                renderCell: (params) => {
                    return (
                        <div className={classes.actionButtons}>
                            <IconButton
                                edge='start'
                                color='inherit'
                                aria-label='open project'
                                title='Open project'
                                onClick={() => navigateToProject(params.id)}
                            >
                                <OpenInBrowserIcon />
                            </IconButton>
                            <IconButton
                                edge='start'
                                color='inherit'
                                aria-label='delete project'
                                title='Delete project'
                                onClick={() => deleteProject(params.id)}>
                                <DeleteIcon />
                            </IconButton>
                        </div >
                    );
                }
            }
        ]
    );

    return columns;
}
