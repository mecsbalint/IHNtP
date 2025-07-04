import { useState } from "react";
import { signUpUser } from "../services/userAuthService";
import { useNavigate } from "react-router-dom";
import { UserRegistration } from "../types/User";

type UseRegistrationResult = {
    error: "" | "This e-mail is already in use",
    isLoading: boolean | null,
    signUp: (submitObj: UserRegistration) => Promise<void>
}

export function useRegistration() : UseRegistrationResult {
    const [isLoading, setIsLoading] = useState<boolean | null>(null);
    const [error, setError] = useState<"" | "This e-mail is already in use">("");
    const navigate = useNavigate();
    
    async function signUp(submitObj : UserRegistration) {
        setIsLoading(true);
        setError("");

        const responseStatus = await signUpUser(submitObj);

        if (responseStatus === 409) {
            setError("This e-mail is already in use");
        } else if (responseStatus === 201) {
            navigate("/login");
        }
        
        setIsLoading(false);
    }

    return {error, isLoading, signUp};
}
