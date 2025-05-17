import { useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import UserForm from "../components/UserForm/UserForm";
import { useRegistration } from "../hooks/useRegistration";
import { useAuthContext } from "../hooks/useAuthContext";

function RegistrationPage() {
    const navigate = useNavigate();
    const {error, isLoading, registrate} = useRegistration();
    const {user} = useAuthContext();

    useEffect(() => {
        user && navigate("/solarwatch");
    }, [user, navigate]);

    async function onSubmit(event, submitObj) {
        event.preventDefault();

        await registrate(submitObj);
    }

    return (
        <div className="w-full bg-blue-400 h-[100vh] justify-items-center pt-20">
            <fieldset className="fieldset w-xs bg-base-200 border border-base-300 p-4 rounded-box">
                <div>
                    <legend className="fieldset-legend text-2xl">Registration</legend>
                    <UserForm 
                        submitText={"Sign up"}
                        onSubmit={onSubmit}
                        emailErrorMsg={error}
                        isLoading={isLoading}            
                    />  
                </div>
                <Link className="text text-secondary my-1" to="/login">Do you have already an account?</Link>
            </fieldset>
        </div>
    );
}

export default RegistrationPage;
