import { TaskStatus } from "./TaskStatus";

export interface TaskDto {
    code: number;
    assignee: string;
    project: number;
    description: string;
    progress: number;
    status: TaskStatus;
    deadline: Date;
}
