
export type User = {
    jwt: string,
    name: string,
    roles: string[]
}

export type UserLogin = {
    email: string,
    password: string
}

export type UserRegistration = UserLogin & {
    name: string
}
