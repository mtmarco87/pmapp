import { IconButton } from "@material-ui/core";
import { GridColDef } from "@material-ui/data-grid";
import OpenInBrowserIcon from '@material-ui/icons/OpenInBrowser';
import DeleteIcon from '@material-ui/icons/Delete';
import { UserDto } from "../../../models/dtos/UserDto";

export const getProjectsColumnsDefs = ({ classes, deleteProject, history, users }:
    { classes: any, deleteProject: Function, history: any, users: UserDto[] }) => {
    const columns: GridColDef[] = (
        [
            { field: 'code', headerName: 'Code', width: 120 },
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
                valueGetter: (params) => {
                    const projectManager = users?.find(u => u.username === params.value);

                    return !!projectManager ?
                        `${projectManager?.name} ${projectManager?.surname}`
                        :
                        'N/A';
                }                
            },
            {
                field: "",
                headerName: "Actions",
                width: 150,
                renderCell: (params) => {
                    return (
                        <div className={classes.actionButtons}>
                            <IconButton
                                edge="start"
                                color="inherit"
                                aria-label="open project"
                                onClick={() => history.push("/projects/" + params.id)}
                            >
                                <OpenInBrowserIcon />
                            </IconButton>
                            <IconButton
                                edge="start"
                                color="inherit"
                                aria-label="delete project"
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
