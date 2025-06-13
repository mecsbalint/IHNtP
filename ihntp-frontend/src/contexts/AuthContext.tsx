import { useEffect, useReducer } from "react";
import { createContext } from "react";
import { AuthState } from "../types/AuthState";
import { AuthContextType } from "../types/AuthContextType";
import { AuthAction } from "../types/AuthAction";
import { User } from "../types/User";

export const AuthContext = createContext<AuthContextType | null>(null);

export function authReducer(state : AuthState, action : AuthAction) {
    switch (action.type) {
        case "LOGIN":
            return {user: action.payload, isLoggedIn: true}
        case "LOGOUT":
            return {user: null, isLoggedIn: false}
        default:
            return state;
    }
}

export function AuthContextProvider({children} : React.PropsWithChildren) {
    const [state, dispatch] = useReducer(authReducer, {
        user: null,
        isLoggedIn: null
    } as AuthState);

    useEffect(() => {
        let user : User | null; 
        
        let userStringified : string | null = localStorage.getItem("ihntpUser");

        if (userStringified !== null) {
            user = JSON.parse(userStringified);
        } else {
            user = null;
        }
        
        user && dispatch({type: "LOGIN", payload: user});
    }, []);

    console.log("AuthContext state: " + state);

    return (
        <AuthContext.Provider value={{...state, dispatch}} >
            {children}
        </AuthContext.Provider>
    )
}
