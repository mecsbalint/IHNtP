import { useAuthContext } from "./useAuthContext";
import { useNavigate } from "react-router-dom";

export function useLogout() : () => Promise<void> {
    const {dispatch} = useAuthContext();
    const navigate = useNavigate();

    async function logout() {
        localStorage.setItem("ihntpUser", "null");
        dispatch({type: "LOGOUT"});
        navigate("/");
    }

    return logout;
}
