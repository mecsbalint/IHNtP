import { useNavigate } from "react-router-dom";

function useUnauthorizedHandler() {
    const navigate = useNavigate();

    return function handleUnauthorizedResponse() {
        localStorage.setItem("ihntpJwt", null);
        localStorage.setItem("ihntpUsername", null);
        navigate("/login");
        window.location.reload();
    }
}

export {useUnauthorizedHandler};
