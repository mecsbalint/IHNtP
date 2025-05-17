import { useNavigate } from "react-router-dom";
import { loginUser } from "../services/userAuthService";
import {useAuthContext} from "./useAuthContext";
import { useState } from "react";

export function useLogin() {
    const [isLoading, setIsLoading] = useState(null);
    const [error, setError] = useState("");
    const navigate = useNavigate();
    const {dispatch} = useAuthContext();

    async function login(submitObj) {
        setIsLoading(true);
        setError("");

        const loginObj = {
            email: submitObj.email,
            password: submitObj.password
        };

        const response = await loginUser(loginObj);
        
        if (response.status === 401) {
            setError("Incorrect email or password!");
        } else if (response.status === 200) {
            localStorage.setItem("ihntpUser", JSON.stringify(response.body));
            dispatch({type: "LOGIN", payload: response.body});
            navigate("/");
        }
        
        setIsLoading(false);
    }

    return {error, isLoading, login};
}
