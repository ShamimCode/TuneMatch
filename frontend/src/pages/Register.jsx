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
          Create account
        </h2>

        {error && (
          <div className="bg-[#FF6B4A]/10 text-[#FF6B4A] font-mono-ui text-sm p-3 rounded mb-4 border border-[#FF6B4A]/20">
            {error}
          </div>
        )}

        {success && (
          <div className="bg-[#6EE7B7]/10 text-[#6EE7B7] font-mono-ui text-sm p-3 rounded mb-4 border border-[#6EE7B7]/20">
            Registered. Redirecting to login...
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

        <div className="mb-4">
          <label className="block text-[#9490B0] text-sm mb-1.5">Email</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
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
          disabled={loading || success}
          className="w-full bg-[#FF3D7F] hover:bg-[#FF3D7F]/90 text-[#14121F] py-2.5 rounded font-display font-semibold disabled:opacity-50 transition">
          {loading ? "Registering..." : "Register"}
        </button>

        <p className="text-[#9490B0] text-sm mt-5 text-center">
          Already have an account?{" "}
          <Link to="/login" className="text-[#FF3D7F] hover:underline">
            Log in
          </Link>
        </p>
      </form>
    </div>
  );
}

export default Register;
