import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import UserForm from "../components/UserForm/UserForm";

function RegistrationPage() {
    const navigate = useNavigate();

    const [emailErrorMsg, setEmailErrorMsg] = useState("");

    useEffect(() => {
        [null, "null"].includes(localStorage.getItem("solarWatchJwt")) || navigate("/solarwatch");
    }, [navigate]);

    async function onSubmit(event, submitObj) {
        event.preventDefault();

        setEmailErrorMsg("");

        const registrationObj = {
            email: submitObj.email,
            name: submitObj.name,
            password: submitObj.password
        }

        const response = await fetch("api/registration", {
                method: "POST", 
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(registrationObj)
            }
        );

        if (response.status === 409) {
            setEmailErrorMsg("This e-mail is already in use.")
        } else if (response.status === 201) {
            navigate("/login")
        }
    }

    return (
        <div className="w-full bg-blue-400 h-[100vh] justify-items-center pt-20">
            <fieldset className="fieldset w-xs bg-base-200 border border-base-300 p-4 rounded-box">
                <div>
                    <legend className="fieldset-legend text-2xl">Registration</legend>
                    <UserForm 
                        submitText={"Sign up"}
                        onSubmit={onSubmit}
                        emailErrorMsg={emailErrorMsg}             
                    />  
                </div>
                <Link className="text text-secondary my-1" to="/login">Do you have already an account?</Link>
            </fieldset>
        </div>
    );
}

export default RegistrationPage;
