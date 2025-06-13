import { User } from "./User"

export type AuthState = {
    user: User | null,
    isLoggedIn: boolean | null
}