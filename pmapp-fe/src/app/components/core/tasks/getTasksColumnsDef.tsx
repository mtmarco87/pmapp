import { IconButton } from "@material-ui/core";
import { GridColDef } from "@material-ui/data-grid";
import DeleteIcon from '@material-ui/icons/Delete';
import moment from 'moment';
import { ProjectDto } from "../../../models/dtos/ProjectDto";
import { Role } from "../../../models/dtos/Role";
import { UserDto } from "../../../models/dtos/UserDto";

export const getTasksColumnsDefs = ({ classes, deleteTask, userRole, users, projects }:
    { classes: any, deleteTask: Function, userRole: Role | undefined, users: UserDto[], projects: ProjectDto[] }) => {
    const columns: GridColDef[] = (
        [
            { field: 'code', headerName: 'Code', width: 120 },
            {
                field: 'assignee',
                headerName: 'Assignee',
                width: 200,
                editable: userRole === Role.Administrator || userRole === Role.ProjectManager,
                valueGetter: (params) => {
                    const assignee = users?.find(u => u.username === params.value);

                    return !!assignee ?
                        `${assignee?.name} ${assignee?.surname}`
                        :
                        'N/A';
                }
            },
            {
                field: 'project',
                headerName: 'Project',
                width: 200,
                editable: userRole === Role.Administrator,
                valueGetter: (params) => {
                    return projects?.find(p => p.code === params.value)?.name;
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
            },
            {
                field: 'status',
                headerName: 'Status',
                width: 130,
                editable: true,
            },
            {
                field: 'deadline',
                headerName: 'Deadline',
                width: 150,
                editable: userRole === Role.Administrator || userRole === Role.ProjectManager,
                // renderCell: (params) => {
                //     const dateStr: string = params?.value as string;
                //     const date = moment(dateStr).format('DD-MM-YYYY');
                //     return (
                //         <div>{date.toString()}</div>
                //     );
                // },
                valueGetter: (params) => {
                    const dateStr: string = params?.value as string;
                    const date = moment(dateStr).format('DD-MM-YYYY');
                    return date.toString();
                }
            },
            {
                field: "",
                headerName: "Actions",
                width: 140,
                renderCell: (params) => {
                    return (
                        <>
                            {userRole !== Role.Developer &&
                                < div className={classes.actionButtons} >
                                    <IconButton
                                        edge="start"
                                        color="inherit"
                                        aria-label="delete task"
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
