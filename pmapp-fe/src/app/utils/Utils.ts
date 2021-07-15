import { GridEditCellProps, GridRowId } from '@material-ui/data-grid';
import { AxiosResponse } from "axios";
import lodash from "lodash";
import moment from 'moment';
import { setNotification } from "../redux/slices/sessionSlice";

export function isInterceptedError(error: any) {
    const status = error?.response?.status;
    return status === 401 || status === 403;
};

export function isNotFound(error: any) {
    const status = error?.response?.status;
    return status === 404;
};

export function getErrorMessage(error: any) {
    let errorMsg = "An unexpected error occurred";
    if (typeof (error?.response?.data) === "string") {
        errorMsg = error?.response?.data;
    } else if (typeof (error?.response?.data?.error) === "string") {
        errorMsg = error?.response?.data?.error;
    }

    return errorMsg;
};

export function handleCellEditWithDbUpdate<T>(
    rows: T[],
    setRows: Function,
    idField: string,
    updateService: (item: T) => Promise<AxiosResponse<T>>,
    dispatch: any,
    successCallback: Function,
    successMessage: string
) {
    // Returns a Material-UI Datagrid edited cell handler
    return ({ id, field, props }:
        { id: GridRowId, field: string, props: GridEditCellProps }) => {
        // Keep a copy of original rows to rollback in case of failure
        const previousRows = lodash.cloneDeep(rows);

        // Handle special cases
        let newValue = props.value;
        if (props.value instanceof Date) {
            // Date columns needs to be converted back to expected format
            newValue = moment(props.value).format('YYYY-MM-DDTHH:mm:ss.SSSZ').toString();
        } else if (field === 'progress') {
            if (props.value as number < 0) {
                newValue = 0;
            } else if (props.value as number > 100) {
                newValue = 100;
            }
        }

        // Update current item and rows in memory with new value
        let itemToUpdate: T = {} as T;
        const updatedRows = rows.map((row: any) => {
            if (row[idField] === id) {
                row[field] = newValue;
                itemToUpdate = row;
            }
            return row;
        });
        setRows(updatedRows);

        // Try to update items on the database
        updateService(itemToUpdate)
            .then(() => {
                // Notify user of success
                dispatch(setNotification({
                    message: successMessage.replace('%s', (itemToUpdate as any)[idField]),
                    type: 'success'
                }));
                if (!!successCallback) {
                    successCallback();
                }
            })
            .catch(() => {
                // Rollback
                setRows(previousRows);
            })
    };
};
