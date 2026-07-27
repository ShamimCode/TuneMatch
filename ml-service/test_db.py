from recommender import ContentRecommender

recommender = ContentRecommender()
print("Model built successfully")

similar = recommender.get_similar_songs("003vvx7Niy0yvhvHt4a68B", top_n=5)
for s in similar:
    print(s)
