/* eslint-disable react/prop-types */
import { useEffect, useReducer } from "react";
import { createContext } from "react";

export const AuthContext = createContext();

export function authReducer(state, action) {
    switch (action.type) {
        case "LOGIN":
            return {user: action.payload, isLoggedIn: true}    
        case "LOGOUT":
            return {user: null, isLoggedIn: false}
        default:
            return state;
    }
}

export function AuthContextProvider({children}) {
    const [state, dispatch] = useReducer(authReducer, {
        user: null,
        isLoggedIn: null
    });

    useEffect(() => {
        let user;
        
        try {
            user = JSON.parse(localStorage.getItem("ihntpUser"));
        } catch {
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
