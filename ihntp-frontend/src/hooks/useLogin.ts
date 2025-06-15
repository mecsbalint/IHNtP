import { useNavigate } from "react-router-dom";
import { loginUser } from "../services/userAuthService";
import {useAuthContext} from "./useAuthContext";
import { useState } from "react";
import { UserLogin } from "../types/User";

type UseLoginResult = {
    error: "" | "Incorrect email or password!",
    isLoading: boolean | null,
    login: (submitObj: UserLogin) => void
}

export function useLogin() : UseLoginResult {
    const [isLoading, setIsLoading] = useState<boolean | null>(null);
    const [error, setError] = useState<"" | "Incorrect email or password!">("");
    const navigate = useNavigate();
    const {dispatch} = useAuthContext();

    async function login(submitObj : UserLogin) {
        setIsLoading(true);
        setError("");

        const response = await loginUser(submitObj);
        
        if (response.status === 401) {
            setError("Incorrect email or password!");
        } else if (response.status === 200 && response.body) {
            localStorage.setItem("ihntpUser", JSON.stringify(response.body));
            dispatch({type: "LOGIN", payload: response.body});
            navigate("/");
        }
        
        setIsLoading(false);
    }

    return {error, isLoading, login};
}
