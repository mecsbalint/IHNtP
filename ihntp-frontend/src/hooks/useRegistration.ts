import { useState } from "react";
import { registrateUser } from "../services/userAuthService";
import { useNavigate } from "react-router-dom";
import { UserRegistration } from "../types/User";

type UseRegistrationResult = {
    error: "" | "This e-mail is already in use",
    isLoading: boolean | null,
    registrate: (submitObj: UserRegistration) => Promise<void>
}

export function useRegistration() : UseRegistrationResult {
    const [isLoading, setIsLoading] = useState<boolean | null>(null);
    const [error, setError] = useState<"" | "This e-mail is already in use">("");
    const navigate = useNavigate();
    
    async function registrate(submitObj : UserRegistration) {
        setIsLoading(true);
        setError("");

        const responseStatus = await registrateUser(submitObj);

        if (responseStatus === 409) {
            setError("This e-mail is already in use");
        } else if (responseStatus === 201) {
            navigate("/login");
        }
        
        setIsLoading(false);
    }

    return {error, isLoading, registrate};
}
