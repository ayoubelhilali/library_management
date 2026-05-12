import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import API from "../api/api";

function Login() {
  const navigate = useNavigate();

  const [credentials, setCredentials] = useState({
    username: "",
    password: "",
  });

  const [error, setError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;

    setCredentials((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const response = await API.post("/login", credentials);

      localStorage.setItem("user", JSON.stringify(response.data));

      navigate("/dashboard");
    } catch (err) {
      setError("Invalid username or password");
    }
  };

  return (
    <div className="min-h-screen bg-slate-900 flex">
      <div className="hidden lg:flex lg:w-1/2 relative items-center justify-center">
        <img
          src="https://images.unsplash.com/photo-1481627834876-b7833e8f5570?auto=format&fit=crop&w=1000&q=80"
          alt="Library Books"
          className="absolute inset-0 w-full h-full object-cover"
        />

        <div className="absolute inset-0 bg-indigo-900/40"></div>

        <div className="z-20 relative p-12 text-center">
          <h2 className="text-4xl font-bold text-white mb-4">
            Welcome back to your library.
          </h2>

          <p className="text-lg text-indigo-100 max-w-md mx-auto">
            Log in to manage books, borrows, and your library account.
          </p>
        </div>
      </div>

      <div className="w-full lg:w-1/2 flex items-center justify-center p-8 sm:p-12">
        <div className="max-w-md w-full bg-slate-800/50 border border-slate-700 p-8 rounded-3xl shadow-2xl">
          <div className="text-center mb-8">
            <span className="uppercase font-bold tracking-widest text-indigo-400 mb-2 block text-sm">
              Welcome Back
            </span>

            <h1 className="text-3xl font-bold text-white">
              Log in to your account
            </h1>
          </div>

          {error && (
            <div className="mb-4 bg-red-500/20 border border-red-500 text-red-300 px-4 py-3 rounded-lg">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label
                className="block text-sm font-medium text-slate-300 mb-2"
                htmlFor="username"
              >
                Username
              </label>

              <input
                type="text"
                id="username"
                name="username"
                value={credentials.username}
                onChange={handleChange}
                className="w-full bg-slate-900 border border-slate-700 rounded-lg px-4 py-3 text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                placeholder="Enter your username"
                required
              />
            </div>

            <div>
              <label
                className="block text-sm font-medium text-slate-300 mb-2"
                htmlFor="password"
              >
                Password
              </label>

              <input
                type="password"
                id="password"
                name="password"
                value={credentials.password}
                onChange={handleChange}
                className="w-full bg-slate-900 border border-slate-700 rounded-lg px-4 py-3 text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                placeholder="••••••••"
                required
              />
            </div>

            <button
              type="submit"
              className="w-full py-3 px-4 bg-indigo-600 text-white font-medium rounded-lg shadow-lg hover:bg-indigo-700 transition"
            >
              Sign In
            </button>
          </form>

          <p className="mt-8 text-center text-sm text-slate-400">
            Don&apos;t have an account?{" "}
            <Link
              to="/register"
              className="text-indigo-400 hover:text-indigo-300 font-medium"
            >
              Create one now
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}

export default Login;
