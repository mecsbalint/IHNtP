import { useState } from "react";
import { registrateUser } from "../services/userAuthService";
import { useNavigate } from "react-router-dom";

export function useRegistration() {
    const [isLoading, setIsLoading] = useState(null);
    const [error, setError] = useState("");
    const navigate = useNavigate();
    
    async function registrate(submitObj) {
        setIsLoading(true);
        setError("");

        const registrationObj = {
            email: submitObj.email,
            name: submitObj.name,
            password: submitObj.password
        };

        const response = await registrateUser(registrationObj);

        if (response.status === 409) {
            setError("This e-mail is already in use.");
        } else if (response.status === 201) {
            navigate("/login");
        }
        
        setIsLoading(false);
    }

    return {error, isLoading, registrate};
}
