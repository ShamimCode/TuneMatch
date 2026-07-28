import { createContext, useContext, useState } from "react";
import axiosClient from "../api/axiosClient";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem("token"));
  const [username, setUsername] = useState(localStorage.getItem("username"));

  const login = async (usernameInput, password) => {
    const response = await axiosClient.post("/auth/login", {
      username: usernameInput,
      password,
    });

    const { token: newToken, username: returnedUsername } = response.data;

    localStorage.setItem("token", newToken);
    localStorage.setItem("username", returnedUsername);
    setToken(newToken);
    setUsername(returnedUsername);
  };

  const register = async (usernameInput, email, password) => {
    await axiosClient.post("/auth/register", {
      username: usernameInput,
      email,
      password,
    });
  };

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    setToken(null);
    setUsername(null);
  };

  const isAuthenticated = !!token;

  return (
    <AuthContext.Provider
      value={{ token, username, login, register, logout, isAuthenticated }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
