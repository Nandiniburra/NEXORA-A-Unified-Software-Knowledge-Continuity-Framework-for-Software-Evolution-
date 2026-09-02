# Frontend Dockerfile (development: runs react-scripts start so docker-compose can use port 3000)
FROM node:18-alpine

WORKDIR /app

# Install dependencies
COPY package*.json ./
RUN npm ci --silent

# Copy source
COPY . .

# Expose dev port and ensure the dev server listens on all interfaces
ENV PORT=3000
ENV HOST=0.0.0.0
EXPOSE 3000

# Start the React development server
CMD ["npm", "start"]
