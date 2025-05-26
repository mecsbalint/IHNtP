/* eslint-disable react/prop-types */
import { useEffect, useReducer } from "react";
import { createContext } from "react";

export const AuthContext = createContext();

export function authReducer(state, action) {
    switch (action.type) {
        case "LOGIN":
            return {...action.payload}    
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
        let isLoggedIn = null;
        
        try {
            user = JSON.parse(localStorage.getItem("ihntpUser"));
            isLoggedIn = true;
        } catch {
            user = null;
            isLoggedIn = false;
        }
        
        user && dispatch({type: "LOGIN", payload: {user, isLoggedIn}});
    }, []);

    console.log("AuthContext state: " + state);

    return (
        <AuthContext.Provider value={{...state, dispatch}} >
            {children}
        </AuthContext.Provider>
    )
}
