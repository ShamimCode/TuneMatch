import { useState, useEffect } from "react";
import axiosClient from "../api/axiosClient";
import { useAuth } from "../context/AuthContext";

const GENRE_COLORS = {
  pop: "#FF3D7F",
  rock: "#FF6B4A",
  "hip-hop": "#FFD23F",
  edm: "#2DE1FC",
  "r-n-b": "#B24BF3",
  indie: "#6EE7B7",
  jazz: "#FFB627",
  classical: "#C9A5FF",
  country: "#D9A066",
  metal: "#9CA3AF",
  reggae: "#22C55E",
  "k-pop": "#FB7185",
};

function EqualizerMark() {
  return (
    <div className="flex items-end gap-[3px] h-5" aria-hidden="true">
      {[0.5, 1, 0.65, 0.85].map((delay, i) => (
        <span
          key={i}
          className="w-[3px] bg-[#FF3D7F] rounded-full motion-safe:animate-[eq-pulse_1.1s_ease-in-out_infinite]"
          style={{
            height: "100%",
            animationDelay: `${delay * 0.15}s`,
          }}
        />
      ))}
    </div>
  );
}

function Home() {
  const [songs, setSongs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const { username, logout } = useAuth();

  useEffect(() => {
    const fetchSongs = async () => {
      try {
        const response = await axiosClient.get("/songs");
        setSongs(response.data);
      } catch (err) {
        setError("Couldn't load songs. Try refreshing.");
      } finally {
        setLoading(false);
      }
    };

    fetchSongs();
  }, []);

  return (
    <div className="min-h-screen bg-[#14121F] text-[#F3F0FF] font-body">
      <div className="flex justify-between items-center px-8 py-6 border-b border-white/10">
        <div className="flex items-center gap-3">
          <EqualizerMark />
          <h1 className="text-2xl font-display font-bold tracking-tight">
            TuneMatch
          </h1>
        </div>
        <div className="flex items-center gap-4">
          <span className="text-[#9490B0] text-sm">
            Signed in as <span className="text-[#F3F0FF]">{username}</span>
          </span>
          <button
            onClick={logout}
            className="border border-white/15 hover:border-[#FF3D7F]/50 hover:text-[#FF3D7F] transition px-4 py-1.5 rounded-full text-sm">
            Log Out
          </button>
        </div>
      </div>

      <div className="px-8 py-8">
        {loading && (
          <p className="text-[#9490B0] font-mono-ui text-sm">
            Loading tracks...
          </p>
        )}
        {error && (
          <p className="text-[#FF6B4A] font-mono-ui text-sm">{error}</p>
        )}

        {!loading && !error && (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {songs.slice(0, 30).map((song) => {
              const accent = GENRE_COLORS[song.genre] || "#9490B0";
              return (
                <div
                  key={song.trackId}
                  className="bg-[#1E1B2E] rounded-lg p-4 pl-5 hover:bg-[#25213A] transition cursor-pointer"
                  style={{ borderLeft: `3px solid ${accent}` }}>
                  <h2 className="font-display font-medium truncate">
                    {song.title}
                  </h2>
                  <p className="text-[#9490B0] text-sm truncate mt-0.5">
                    {song.artist}
                  </p>
                  <span
                    className="inline-block mt-3 text-xs font-mono-ui px-2 py-1 rounded"
                    style={{
                      color: accent,
                      backgroundColor: `${accent}1A`,
                    }}>
                    {song.genre}
                  </span>
                </div>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
}

export default Home;
