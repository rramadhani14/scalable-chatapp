export class LoginDto {
    constructor(readonly username: string, readonly password: string, readonly csrf: string) {}
}