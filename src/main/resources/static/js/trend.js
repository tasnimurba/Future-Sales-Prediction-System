document.addEventListener("DOMContentLoaded", function () {
    fetch('/api/prediction-trends')
        .then(response => response.json())
        .then(data => {
            if (!Array.isArray(data)) throw new Error("Invalid data format");

            const labels = data.map(d => d.date);
            const actual = data.map(d => d.actual);
            const predicted = data.map(d => d.predicted);

            // ====== Chart rendering ======
            const ctx = document.getElementById('trendChart').getContext('2d');
            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [
                        {
                            label: 'Actual Sales',
                            data: actual,
                            borderColor: '#00e5ff',
                            backgroundColor: '#00e5ff',
                            fill: false,
                            tension: 0.4
                        },
                        {
                            label: 'Predicted Sales',
                            data: predicted,
                            borderColor: '#ff4081',
                            backgroundColor: '#ff4081',
                            fill: false,
                            tension: 0.4
                        }
                    ]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            labels: { color: '#ffffff' }
                        },
                        title: {
                            display: true,
                            text: 'Sales Trend',
                            color: '#ffffff',
                            font: { size: 20 }
                        }
                    },
                    scales: {
                        x: {
                            ticks: {
                                color: '#ffffff',         // White x-axis labels
                                font: {
                                    size: 12
                                }
                            },
                            grid: {
                                color: '#555555'          // Soft grid line for x
                            },
                            title: {
                                display: true,
                                text: 'Date',
                                color: '#ffffff',
                                font: {
                                    size: 14,
                                    weight: 'bold'
                                }
                            }
                        },
                        y: {
                            beginAtZero: true,
                            ticks: {
                                color: '#ffffff',         // White y-axis labels
                                font: {
                                    size: 12
                                }
                            },
                            grid: {
                                color: '#555555'          // Soft grid line for y
                            },
                            title: {
                                display: true,
                                text: 'Sales (Units)',
                                color: '#ffffff',
                                font: {
                                    size: 14,
                                    weight: 'bold'
                                }
                            }
                        }
                    }
                }
            });

            // ====== Sales vs Prediction Bars ======
            const summaryContainer = document.getElementById("salesSummary");
            summaryContainer.innerHTML = "<h3>Sales vs Predictions</h3>";

            data.forEach((entry) => {
                const actualVal = entry.actual;
                const predictedVal = entry.predicted;

                const row = `
                  <div class="bar-row">
                    <span>${entry.date}</span>
                    <div class="bar-wrapper">
                      <div class="bar-actual"><span>Actual: $${actualVal}</span></div>
                      <div class="bar-predicted"><span>Predicted: $${predictedVal}</span></div>
                    </div>
                  </div>
                `;

                summaryContainer.innerHTML += row;
            });

            // ====== Next Month Prediction Box ======
            const nextBox = document.getElementById("nextMonthPrediction");
            const lastIndex = data.length - 1;
            if (lastIndex >= 1) {
                const prev = data[lastIndex - 1].predicted;
                const curr = data[lastIndex].predicted;
                const change = ((curr - prev) / prev * 100).toFixed(1);

                nextBox.innerHTML = `
                    <div class="forecast-box">
                        <h3>Next Month Prediction</h3>
                        <div class="prediction-value">$${curr}</div>
                        <div class="prediction-note">${change > 0 ? "+" : ""}${change}% from last month</div>
                    </div>
                `;
            }

        })
        .catch(err => {
            console.error('Fetch or render error:', err);
            document.getElementById('trendChart').innerHTML = "⚠️ Failed to load chart.";
        });
});
