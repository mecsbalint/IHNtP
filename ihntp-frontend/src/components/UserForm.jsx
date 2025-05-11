import { useEffect, useState } from "react";

function UserForm({submitText, onSubmit, nameErrorMsg = "", emailErrorMsg = "", passwordErrorMsg = ""}) {
  const [name, setName] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [emailErrorState, setEmailErrorState] = useState(false);
  const [passwordErrorState, setPasswordErrorState] = useState(false);

  useEffect(() => {
    emailErrorMsg && setEmailErrorState(true);
    passwordErrorMsg && setPasswordErrorState(true);
  }, [emailErrorMsg, passwordErrorMsg, nameErrorMsg]);

  useEffect(() => {
    setEmailErrorState(false);
    setPasswordErrorState(false);
  }, [email, password]);
  
  return (
    <form onSubmit={(e) => onSubmit(e, {email: email, name: name, password: password})}>
        <div className={`my-5 ${submitText === "Log in" ? "hidden" : ""}`}>
        <label className="fieldset-label">Username</label>
        <input
          type="text"
          className={`input`}
          placeholder="username"
          onChange={(event) => {
            setName(event.target.value);
          }}
          value={name}
        />
      </div>

      <div className="my-5">
        <label className="fieldset-label">E-mail</label>
        <input
          type="text"
          className={`input ${emailErrorState && "input-error"}`}
          placeholder="e-mail"
          onChange={(event) => {
            setEmail(event.target.value);
            setEmailErrorState(false);
          }}
          value={email}
        />
        <p className="text-error">{emailErrorState ? emailErrorMsg : ""}</p>
      </div>

      <div className="my-5">
        <label className="fieldset-label">Password</label>
        <input
          type="password"
          className={`input ${passwordErrorState && "input-error"}`}
          placeholder="password"
          onChange={(event) => {
            setPassword(event.target.value);
            setPasswordErrorState(false);
          }}
          value={password}
        />
        <p className="text-error">{passwordErrorState ? passwordErrorMsg : ""}</p>
      </div>

      <button type="submit" className="btn btn-neutral mt-4">
        {submitText}
      </button>
    </form>
  );
}

export default UserForm;
