import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import UserForm from "../components/UserForm";

function LoginPage() {
    const navigate = useNavigate();

    const [emailErrorMsg, setEmailErrorMsg] = useState("");
    const [passwordErrorMsg, setPasswordErrorMsg] = useState("");

    useEffect(() => {
        [null, "null"].includes(localStorage.getItem("ihntpJwt")) || navigate("/");
    }, [navigate]);

    async function onSubmit(event, submitObj) {
        event.preventDefault();

        setEmailErrorMsg("");
        setPasswordErrorMsg("");

        const loginObj = {
            email: submitObj.email,
            password: submitObj.password
        }

        const response = await fetch("api/login", {
                method: "POST", 
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(loginObj)
            }
        );

        if (response.status === 401) {
            setEmailErrorMsg("Incorrect email or password!");
            setPasswordErrorMsg("Incorrect email or password!");
        } else if (response.status === 200) {
            const responseBody = await response.json();
            localStorage.setItem("ihntpJwt", responseBody.jwt);
            localStorage.setItem("ihntpUsername", responseBody.name);
            navigate("/");
            window.location.reload();
        }
    }

    return (
        <div className="w-full bg-blue-400 h-[100vh] justify-items-center pt-20">
            <fieldset className="fieldset w-xs bg-base-200 border border-base-300 p-4 rounded-box">
                <div>
                    <legend className="fieldset-legend text-2xl">Login</legend>
                    <UserForm 
                        submitText={"Log in"}
                        onSubmit={onSubmit}
                        emailErrorMsg={emailErrorMsg}
                        passwordErrorMsg={passwordErrorMsg}                  
                    />  
                </div>
                <Link className="text text-secondary my-1" to="/registration">Don't have an account?</Link>
            </fieldset>
        </div>
    )
}

export default LoginPage;
