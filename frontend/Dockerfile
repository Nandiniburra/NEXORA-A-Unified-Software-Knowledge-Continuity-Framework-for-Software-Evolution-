# Production-ready multi-stage Dockerfile for the frontend
# Stage 1: build the React app
FROM node:18-alpine AS build
WORKDIR /app

# Install dependencies
COPY package*.json ./
RUN npm ci --silent

# Copy source and build
COPY . .
RUN npm run build

# Stage 2: runtime - serve the built static files using 'serve'
FROM node:18-alpine AS runtime
WORKDIR /app

# Install a lightweight static server
RUN npm install -g serve@14.1.2 --silent

# Copy build artifacts
COPY --from=build /app/build ./build

# Listen on port 3000 to match docker-compose mapping
ENV PORT=3000
EXPOSE 3000

# Serve the static build
CMD ["serve", "-s", "build", "-l", "3000"]
