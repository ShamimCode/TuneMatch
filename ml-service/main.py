from fastapi import FastAPI, HTTPException

from recommender import ContentRecommender

app = FastAPI(title="TuneMatch ML Service")

recommender = ContentRecommender()


@app.get("/health")
def health_check():
    return {"status": "ok"}


@app.get("/recommendations/{track_id}")
def get_recommendations(track_id: str, top_n: int = 10):
    results = recommender.get_similar_songs(track_id, top_n=top_n)

    if not results:
        raise HTTPException(status_code=404, detail="Track not found")

    return {
        "source_track_id": track_id,
        "recommendations": results,
    }