import { useState } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate, Link } from "react-router-dom";

const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      await login(username, password);
      navigate("/");
    } catch (err) {
      setError("Invalid username or password");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-[#14121F] font-body">
      <form
        onSubmit={handleSubmit}
        className="bg-[#1E1B2E] p-8 rounded-lg shadow-xl w-full max-w-sm border border-white/5">
        <div className="flex items-center gap-3 mb-8">
          <div className="flex items-end gap-[3px] h-5" aria-hidden="true">
            {[0.5, 1, 0.65, 0.85].map((delay, i) => (
              <span
                key={i}
                className="w-[3px] bg-[#FF3D7F] rounded-full motion-safe:animate-[eq-pulse_1.1s_ease-in-out_infinite]"
                style={{ height: "100%", animationDelay: `${delay * 0.15}s` }}
              />
            ))}
          </div>
          <h1 className="text-xl font-display font-bold text-[#F3F0FF] tracking-tight">
            TuneMatch
          </h1>
        </div>

        <h2 className="text-lg font-display font-medium text-[#F3F0FF] mb-6">
          Log in
        </h2>

        {error && (
          <div className="bg-[#FF6B4A]/10 text-[#FF6B4A] font-mono-ui text-sm p-3 rounded mb-4 border border-[#FF6B4A]/20">
            {error}
          </div>
        )}

        <div className="mb-4">
          <label className="block text-[#9490B0] text-sm mb-1.5">
            Username
          </label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
            className="w-full px-3 py-2.5 rounded bg-[#14121F] text-[#F3F0FF] border border-white/10 focus:outline-none focus:border-[#FF3D7F]/60 transition"
          />
        </div>

        <div className="mb-6">
          <label className="block text-[#9490B0] text-sm mb-1.5">
            Password
          </label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            className="w-full px-3 py-2.5 rounded bg-[#14121F] text-[#F3F0FF] border border-white/10 focus:outline-none focus:border-[#FF3D7F]/60 transition"
          />
        </div>

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-[#FF3D7F] hover:bg-[#FF3D7F]/90 text-[#14121F] py-2.5 rounded font-display font-semibold disabled:opacity-50 transition">
          {loading ? "Logging in..." : "Log in"}
        </button>

        <p className="text-[#9490B0] text-sm mt-5 text-center">
          Don't have an account?{" "}
          <Link to="/register" className="text-[#FF3D7F] hover:underline">
            Register
          </Link>
        </p>
      </form>
    </div>
  );
};

export default Login;
