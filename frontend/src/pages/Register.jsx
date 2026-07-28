import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function Register() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(false);

  const { register } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      await register(username, email, password);
      setSuccess(true);
      setTimeout(() => navigate("/login"), 1500);
    } catch (err) {
      const message =
        err.response?.data?.error || "Registration failed. Try again.";
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900">
      <form
        onSubmit={handleSubmit}
        className="bg-gray-800 p-8 rounded-lg shadow-lg w-full max-w-sm">
        <h1 className="text-2xl font-bold text-white mb-6">Register</h1>

        {error && (
          <div className="bg-red-500/20 text-red-400 text-sm p-2 rounded mb-4">
            {error}
          </div>
        )}

        {success && (
          <div className="bg-green-500/20 text-green-400 text-sm p-2 rounded mb-4">
            Registered! Redirecting to login...
          </div>
        )}

        <div className="mb-4">
          <label className="block text-gray-300 text-sm mb-1">Username</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
            className="w-full px-3 py-2 rounded bg-gray-700 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <div className="mb-4">
          <label className="block text-gray-300 text-sm mb-1">Email</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            className="w-full px-3 py-2 rounded bg-gray-700 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <div className="mb-6">
          <label className="block text-gray-300 text-sm mb-1">Password</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            className="w-full px-3 py-2 rounded bg-gray-700 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <button
          type="submit"
          disabled={loading || success}
          className="w-full bg-blue-600 hover:bg-blue-700 text-white py-2 rounded font-semibold disabled:opacity-50">
          {loading ? "Registering..." : "Register"}
        </button>

        <p className="text-gray-400 text-sm mt-4 text-center">
          Already have an account?{" "}
          <Link to="/login" className="text-blue-400 hover:underline">
            Log In
          </Link>
        </p>
      </form>
    </div>
  );
}

export default Register;
