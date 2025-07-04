import { FormEvent, useEffect, useState } from "react";
import { UserRegistration } from "../../types/User";
import twoDigitsCountryCodes from "iso-3166-1-alpha-2";

type UserFormProps = {
  submitText: string,
  onSubmit: (event: FormEvent<HTMLFormElement>, formData: UserRegistration) => Promise<void>,
  nameErrorMsg?: string,
  emailErrorMsg?: string,
  passwordErrorMsg?: string,
  isLoading?: boolean | null
  countries?: string[]
}

function UserForm({submitText, onSubmit, nameErrorMsg = "", emailErrorMsg = "", passwordErrorMsg = "", isLoading = null, countries = []} : UserFormProps) {
  const [name, setName] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [countryCode, setCountryCode] = useState("")
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
    <form onSubmit={(e) => onSubmit(e, {email: email, name: name, password: password, countryCode: countryCode})}>
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

      <div className={`my-5 ${submitText === "Log in" ? "hidden" : ""}`}>
        <label className="fieldset-label">Country</label>
        <label className="select pl-0" style={countryCode === "" ? { color: "#9CA3AF" } : {}}>
          <select  className="select ml-0!" onChange={e => setCountryCode(e.target.selectedOptions[0].value)}>
            <option selected={true} disabled={true}>Choose a country</option>
            {countries.map(country => (<option className="text-black" value={twoDigitsCountryCodes.getCode(country) ?? "n/a"}>{country}</option>))}
          </select>
        </label>
      </div>

      <button type="submit" className="btn btn-neutral mt-4" disabled={!!isLoading || (submitText === "Sign up" && countryCode.length === 0)}>
        {submitText}
      </button>
    </form>
  );
}

export default UserForm;
