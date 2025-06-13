import { AuthAction } from "./AuthAction"
import { AuthState } from "./AuthState"
import { User } from "./User"

export type AuthContextType = AuthState & {
    dispatch: React.Dispatch<AuthAction>
}
