import { IconButton } from "@material-ui/core";
import { GridColDef } from "@material-ui/data-grid";
import DeleteIcon from '@material-ui/icons/Delete';
import { Role } from "../../../models/dtos/Role";
import { DataGridSelectField } from "../../shared/DatagridSelectField/DatagridSelectField";

export const getUsersColumnsDefs = ({ classes, deleteUser }: { classes: any, deleteUser: Function }) => {
    const columns: GridColDef[] = [
        {
            field: 'username',
            headerName: 'Username',
            width: 210
        },
        {
            field: 'password',
            headerName: 'Password',
            width: 210,
            editable: true
        },
        {
            field: 'email',
            headerName: 'Email',
            width: 210,
            editable: true,
        },
        {
            field: 'name',
            headerName: 'Name',
            width: 210,
            editable: true,
        },
        {
            field: 'surname',
            headerName: 'Surname',
            width: 210,
            editable: true,
        },
        {
            field: 'role',
            headerName: 'Role',
            width: 210,
            editable: true,
            renderEditCell: (params) => {
                return DataGridSelectField(
                    {
                        options: [Role.Developer, Role.ProjectManager, Role.Administrator],
                        emptyOptionDisabled: true
                    },
                    params
                );
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
                            aria-label="delete user"
                            title="Delete user"
                            onClick={() => deleteUser(params.id)}>
                            <DeleteIcon />
                        </IconButton>
                    </div >
                );
            }
        }
    ];

    return columns;
}
