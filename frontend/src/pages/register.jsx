import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import API from "../api/api";

function Register() {
  const navigate = useNavigate();

 const [credentials, setCredentials] = useState({
   email: "",
   phone: "",
   username: "",
   password: "",
   confirmPassword: "",
   memberType: "STUDENT",
 });

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCredentials((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    setError("");
    setSuccess("");

    if (credentials.password !== credentials.confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    try {
      const payload = {
        username: credentials.username,
        email: credentials.email,
        phone: credentials.phone,
        password: credentials.password,
        memberType: credentials.memberType,
      };

      await API.post("/register", payload);

      setSuccess("Account created successfully");

      setTimeout(() => {
        navigate("/login");
      }, 1500);
    } catch (err) {
      setError("Registration failed");
    }
  };

  return (
    <div className="min-h-screen bg-slate-900 flex">
      <div className="w-full lg:w-1/2 flex items-center justify-center p-8 sm:p-12">
        <div className="max-w-md w-full bg-slate-800/50 border border-slate-700 p-8 rounded-3xl shadow-2xl">
          <div className="text-center mb-8">
            <span className="uppercase font-bold tracking-widest text-indigo-400 mb-2 block text-sm">
              Create your account
            </span>

            <h1 className="text-3xl font-bold text-white">
              Sign up to continue
            </h1>
          </div>

          {error && (
            <div className="mb-4 bg-red-500/20 border border-red-500 text-red-300 px-4 py-3 rounded-lg">
              {error}
            </div>
          )}

          {success && (
            <div className="mb-4 bg-green-500/20 border border-green-500 text-green-300 px-4 py-3 rounded-lg">
              {success}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-5">
            <input
              type="email"
              name="email"
              value={credentials.email}
              onChange={handleChange}
              className="w-full bg-slate-900 border border-slate-700 rounded-lg px-4 py-3 text-white"
              placeholder="Email address"
              required
            />

            <input
              type="text"
              name="phone"
              value={credentials.phone}
              onChange={handleChange}
              className="w-full bg-slate-900 border border-slate-700 rounded-lg px-4 py-3 text-white"
              placeholder="Phone number"
              required
            />

            <input
              type="text"
              name="username"
              value={credentials.username}
              onChange={handleChange}
              className="w-full bg-slate-900 border border-slate-700 rounded-lg px-4 py-3 text-white"
              placeholder="Username"
              required
            />

            <select
              name="memberType"
              value={credentials.memberType}
              onChange={handleChange}
              className="w-full bg-slate-900 border border-slate-700 rounded-lg px-4 py-3 text-white"
            >
              <option value="STUDENT">Student</option>
              <option value="TEACHER">Teacher</option>
            </select>

            <input
              type="password"
              name="password"
              value={credentials.password}
              onChange={handleChange}
              className="w-full bg-slate-900 border border-slate-700 rounded-lg px-4 py-3 text-white"
              placeholder="Password"
              required
            />

            <input
              type="password"
              name="confirmPassword"
              value={credentials.confirmPassword}
              onChange={handleChange}
              className="w-full bg-slate-900 border border-slate-700 rounded-lg px-4 py-3 text-white"
              placeholder="Confirm password"
              required
            />

            <button
              type="submit"
              className="w-full py-3 px-4 bg-indigo-600 text-white font-medium rounded-lg shadow-lg hover:bg-indigo-700 transition"
            >
              Sign Up
            </button>
          </form>

          <p className="mt-8 text-center text-sm text-slate-400">
            Already have an account?{" "}
            <Link to="/login" className="text-indigo-400 hover:text-indigo-300">
              Login
            </Link>
          </p>
        </div>
      </div>

      <div className="hidden lg:flex lg:w-1/2 relative items-center justify-center">
        <img
          src="https://images.unsplash.com/photo-1481627834876-b7833e8f5570?auto=format&fit=crop&w=1000&q=80"
          alt="Library Books"
          className="absolute inset-0 w-full h-full object-cover"
        />

        <div className="absolute inset-0 bg-indigo-900/40"></div>

        <div className="z-20 relative p-12 text-center">
          <h2 className="text-4xl font-bold text-white mb-4">
            Discover a smarter way to explore and manage your library.
          </h2>

          <p className="text-lg text-indigo-100 max-w-md mx-auto">
            Create your account to access books, track borrowings, and enjoy a
            seamless reading experience.
          </p>
        </div>
      </div>
    </div>
  );
}

export default Register;
