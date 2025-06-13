import { User } from "./User";

export type AuthAction = 
    | {type: "LOGIN", payload: User}
    | {type: "LOGOUT"};
