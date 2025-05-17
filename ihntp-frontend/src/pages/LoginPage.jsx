import { useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import UserForm from "../components/UserForm/UserForm";
import { useLogin } from "../hooks/useLogin";
import { useAuthContext } from "../hooks/useAuthContext";

function LoginPage() {
    const navigate = useNavigate();
    const {error, isLoading, login} = useLogin();
    const {user} = useAuthContext();

    useEffect(() => {
        user && navigate("/");
    }, [user, navigate]);

    async function onSubmit(event, submitObj) {
        event.preventDefault();

        await login(submitObj);
    }

    return (
        <div className="w-full bg-blue-400 h-[100vh] justify-items-center pt-20">
            <fieldset className="fieldset w-xs bg-base-200 border border-base-300 p-4 rounded-box">
                <div>
                    <legend className="fieldset-legend text-2xl">Login</legend>
                    <UserForm 
                        submitText={"Log in"}
                        onSubmit={onSubmit}
                        emailErrorMsg={error}
                        passwordErrorMsg={error}
                        isLoading={isLoading}
                    />  
                </div>
                <Link className="text text-secondary my-1" to="/registration">Don't have an account?</Link>
            </fieldset>
        </div>
    )
}

export default LoginPage;
