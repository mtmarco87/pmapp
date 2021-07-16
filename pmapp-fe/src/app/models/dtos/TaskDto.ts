import { TaskStatus } from "./TaskStatus";

export interface TaskDto {
    code?: number | null;
    assignee?: string | null;
    project?: number | null;
    description?: string;
    progress?: number;
    status?: TaskStatus;
    deadline?: string;
}
