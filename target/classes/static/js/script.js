
    let temperatureChart = null;
    let precipitationChart = null;

    function openTab(evt, tabName) {
    // Cacher tous les contenus d'onglets
    const tabContents = document.getElementsByClassName("tab-content");
    for (let i = 0; i < tabContents.length; i++) {
    tabContents[i].classList.remove("active");
}

    // Retirer la classe active de tous les boutons
    const tabButtons = document.getElementsByClassName("tab-button");
    for (let i = 0; i < tabButtons.length; i++) {
    tabButtons[i].classList.remove("active");
}

    // Afficher l'onglet sélectionné et activer le bouton
    document.getElementById(tabName).classList.add("active");
    evt.currentTarget.classList.add("active");
}

    async function loadMeteo() {
    try {
    const response = await fetch("/api/meteo");
    if (!response.ok) {
    throw new Error(`Erreur HTTP: ${response.status}`);
}
    const data = await response.json();

    if (data.error) {
    throw new Error(data.error);
}

    // Masquer le loader
    document.getElementById('loading').style.display = 'none';

    // Créer les graphiques
    createTemperatureChart(data);
    createPrecipitationChart(data);

} catch (error) {
    console.error('Erreur lors du chargement:', error);
    document.getElementById('loading').innerHTML =
    `<div class="error">Erreur lors du chargement des données: ${error.message}</div>`;
}
}

    function createTemperatureChart(data) {
    const ctx = document.getElementById('temperatureChart').getContext('2d');

    // Détruire l'ancien graphique s'il existe
    if (temperatureChart) {
    temperatureChart.destroy();
}

    temperatureChart = new Chart(ctx, {
    type: 'line',
    data: {
    labels: data.labels.slice(0, 24).map(label => {
    const date = new Date(label);
    return date.getHours() + 'h';
}),
    datasets: [
{
    label: "Paris",
    data: data.valuesParis.slice(0, 24),
    borderColor: "#2196F3",
    backgroundColor: "rgba(33, 150, 243, 0.1)",
    fill: false,
    tension: 0.3,
    borderWidth: 3,
    pointBackgroundColor: "#2196F3",
    pointBorderColor: "#ffffff",
    pointBorderWidth: 2,
    pointRadius: 5
},
{
    label: "Annecy",
    data: data.valuesAnnecy.slice(0, 24),
    borderColor: "#F44336",
    backgroundColor: "rgba(244, 67, 54, 0.1)",
    fill: false,
    tension: 0.3,
    borderWidth: 3,
    pointBackgroundColor: "#F44336",
    pointBorderColor: "#ffffff",
    pointBorderWidth: 2,
    pointRadius: 5
},
{
    label: "Bordeaux",
    data: data.valuesBordeaux.slice(0, 24),
    borderColor: "#4CAF50",
    backgroundColor: "rgba(76, 175, 80, 0.1)",
    fill: false,
    tension: 0.3,
    borderWidth: 3,
    pointBackgroundColor: "#4CAF50",
    pointBorderColor: "#ffffff",
    pointBorderWidth: 2,
    pointRadius: 5
}
    ]
},
    options: {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
    legend: {
    position: 'top',
    labels: {
    padding: 20,
    font: {
    size: 14,
    weight: 'bold'
}
}
},
    title: {
    display: true,
    text: 'Évolution des températures (°C)',
    font: {
    size: 16,
    weight: 'bold'
},
    padding: 20
}
},
    scales: {
    y: {
    beginAtZero: false,
    title: {
    display: true,
    text: 'Température (°C)',
    font: {
    size: 12,
    weight: 'bold'
}
},
    grid: {
    color: 'rgba(0,0,0,0.1)'
}
},
    x: {
    title: {
    display: true,
    text: 'Heures',
    font: {
    size: 12,
    weight: 'bold'
}
},
    grid: {
    color: 'rgba(0,0,0,0.1)'
}
}
}
}
});
}

    function createPrecipitationChart(data) {
    const ctx = document.getElementById('precipitationChart').getContext('2d');

    // Détruire l'ancien graphique s'il existe
    if (precipitationChart) {
    precipitationChart.destroy();
}

    precipitationChart = new Chart(ctx, {
    type: 'bar',
    data: {
    labels: data.labels.slice(0, 24).map(label => {
    const date = new Date(label);
    return date.getHours() + 'h';
}),
    datasets: [
{
    label: "Paris",
    data: data.precipitationParis.slice(0, 24),
    backgroundColor: "rgba(33, 150, 243, 0.7)",
    borderColor: "#2196F3",
    borderWidth: 1
},
{
    label: "Annecy",
    data: data.precipitationAnnecy.slice(0, 24),
    backgroundColor: "rgba(244, 67, 54, 0.7)",
    borderColor: "#F44336",
    borderWidth: 1
},
{
    label: "Bordeaux",
    data: data.precipitationBordeaux.slice(0, 24),
    backgroundColor: "rgba(76, 175, 80, 0.7)",
    borderColor: "#4CAF50",
    borderWidth: 1
}
    ]
},
    options: {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
    legend: {
    position: 'top',
    labels: {
    padding: 20,
    font: {
    size: 14,
    weight: 'bold'
}
}
},
    title: {
    display: true,
    text: 'Probabilité de précipitations (%)',
    font: {
    size: 16,
    weight: 'bold'
},
    padding: 20
}
},
    scales: {
    y: {
    beginAtZero: true,
    max: 100,
    title: {
    display: true,
    text: 'Probabilité (%)',
    font: {
    size: 12,
    weight: 'bold'
}
},
    grid: {
    color: 'rgba(0,0,0,0.1)'
}
},
    x: {
    title: {
    display: true,
    text: 'Heures',
    font: {
    size: 12,
    weight: 'bold'
}
},
    grid: {
    color: 'rgba(0,0,0,0.1)'
}
}
}
}
});
}

    // Charger les données au chargement de la page
    window.addEventListener('load', loadMeteo);

    // Date locale
    const today = new Date();

    // Options
    const options = {weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    document.getElementById("date-du-jour").textContent = "- Données du " + today.toLocaleDateString();
