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
